package ru.jecklandin.life;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicIntegerArray;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlSerializer;

import android.util.Log;
import android.util.Xml;

public class LifeMatrix extends ArrayList<Cell> {
	
	private int mDim;
	
	/**
	 * DOLLARS
	 */
	long mCache = 0;
	
	public long lastTimestamp = 0;
	
	public LifeMatrix(int dim, double density) {
		super(dim*dim);
		mDim = dim;
		
//		try {
//			readFromXml(new FileInputStream("/sdcard/matrix.xml"));
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//		}
		
		for(int i=0; i<dim; ++i)
			for(int j=0; j<dim; ++j) {
				int value = Math.random()<density ? Cell.ALIVE : Cell.DEAD; 
//				int value = 0;
				
//				if ((i == 2 || i == 3 || i == 4) && j == 2) {
//					value = 1;
//				}
				
				add(new Cell(this, j, i, value));
			}
	}
	
	void learn() {
		for (Cell c : this) {
			c.learn();
		}
	}
	
	void change() {
		for (Cell c : this) {
			c.change();
		}
	}
	
	void learnAfter() {
		for (Cell c : this) {
			c.learnAfter();
		}
	}
	
	public void computeNextState() {
		for (Cell c : this) {
			c.computeNextState();
		}
	}
	
	public long countMoney() {
		long sum = 0;
		for (Cell c : this) {
			if (c.status == Cell.ALIVE) {
				switch (c.additStatus) {
				case Cell.OK:
					sum += Math.random()*10+15;
					break;
				case Cell.NEWBORN:
					sum += Math.random()*20;
					break;
				case Cell.DYING:
					sum += Math.random()*5+5;
					break;
				default:
					break;
				}
			}
		}
		return sum;
	}
	
	public Cell get(int x, int y) {
		return get(idx(x, y));
	}
	
	public void setValue(int x, int y, int val) {
		get(x, y).status = val;
	}
	
	public void setAdditValue(int x, int y, int val) {
		get(x, y).additStatus = val;
	}
	
	private int idx(int x, int y) {
		if(x<0)
			x += mDim;
		if(y<0)
			y += mDim;
		x = x%mDim;
		y = y%mDim;
		return x+y*mDim; 
	}
	
	public String toXml(long timestamp){
	    XmlSerializer serializer = Xml.newSerializer();
	    StringWriter writer = new StringWriter();
	    try {
	        serializer.setOutput(writer);
	        serializer.startDocument("UTF-8", true);
	        serializer.startTag("", "elements");
	        serializer.attribute("", "value", mCache+"");
	        if (timestamp !=0 ) {
	        	 serializer.attribute("", "ts", timestamp+"");
	        }
	        for (Cell cell: this){
	            serializer.startTag("", "cell");
	            serializer.attribute("", "x", cell.x+"");
	            serializer.attribute("", "y", cell.y+"");
	            serializer.attribute("", "status", cell.status+"");
	            serializer.attribute("", "addStatus", cell.additStatus+"");
	            serializer.endTag("", "cell");
	        }
	        
	        serializer.endTag("", "elements");
	        serializer.endDocument();
	        return writer.toString();
	    } catch (Exception e) {
	        throw new RuntimeException(e);
	    } 
	}
	
	public void readFromXml(InputStream is) {
		clear();
		MatrixFeedParser p = new MatrixFeedParser(this, is);
		p.parse();
	}
	
	public void readFromFile(String fn) throws FileNotFoundException {
		FileInputStream fis = new FileInputStream(fn);
		readFromXml(fis);
	}
	
	public void writeXmlToFile(String fn) {
		String xml = toXml(lastTimestamp);
		File f= new File(fn);
		FileWriter wr;
		try {
			wr = new FileWriter(f);
			wr.append(xml);
			wr.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public boolean isFired() {
		for (Cell c : this) {
			if (c.status == Cell.ALIVE) {
				return false;
			}
		}
		return true;
	}

	public void writeXmlToFile(String fn, long timestamp) {
		String xml = toXml(timestamp);
		File f= new File(fn);
		FileWriter wr;
		try {
			wr = new FileWriter(f);
			wr.append(xml);
			wr.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}

class MatrixFeedParser {
	
	private InputStream mIs;
	private LifeMatrix mMat;
	
    public MatrixFeedParser(LifeMatrix matr, InputStream is) {
    	mIs = is;
    	mMat = matr;
    }
    
    public void parse() {
        XmlPullParser parser = Xml.newPullParser();
        try {
            // auto-detect the encoding from the stream
            parser.setInput(mIs, null);
            int eventType = parser.getEventType();
            boolean done = false;
            Cell cell = null;
            while (eventType != XmlPullParser.END_DOCUMENT && !done){
            	String name = null;
                switch (eventType){
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    case XmlPullParser.START_TAG:
                    	name = parser.getName();
                        if (name.equalsIgnoreCase("cell")){
                           cell = new Cell(mMat, 0, 0, 0);
                           for (int i = 0; i<parser.getAttributeCount(); ++i) {
                        	   String an = parser.getAttributeName(i);
                        	   if (an.equals("x")) {
                        		   cell.x = Integer.parseInt(parser.getAttributeValue(i));
                        	   } else if (an.equals("y")) {
                        		   cell.y = Integer.parseInt(parser.getAttributeValue(i));
                        	   } else if (an.equals("status")) {
                        		   cell.status = Integer.parseInt(parser.getAttributeValue(i));
                        	   } else if (an.equals("addStatus")) {
                        		   cell.additStatus = Integer.parseInt(parser.getAttributeValue(i));
                        	   }
                           }
                        } else if (name.equalsIgnoreCase("elements")) {
                        	 for (int i = 0; i<parser.getAttributeCount(); ++i) {
                          	   String an = parser.getAttributeName(i);
                          	   if (an.equals("value")) {
                          		   mMat.mCache = Long.parseLong(parser.getAttributeValue(i));
                          	   } else if (an.equals("ts")) {
                          		   mMat.lastTimestamp = Long.parseLong(parser.getAttributeValue(i));
                          	   }
                        	 }
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        name = parser.getName();
                        if (name.equalsIgnoreCase("cell") && cell != null){
                            mMat.add(cell);
                        } 
                        break;
                }
                eventType = parser.next();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

