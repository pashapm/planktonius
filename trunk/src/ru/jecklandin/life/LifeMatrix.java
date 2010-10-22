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
	
	public String toXml(){
	    XmlSerializer serializer = Xml.newSerializer();
	    StringWriter writer = new StringWriter();
	    try {
	        serializer.setOutput(writer);
	        serializer.startDocument("UTF-8", true);
	        serializer.startTag("", "elements");
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
		String xml = toXml();
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
                    	cell = new Cell(mMat, 0, 0, 0);
                        name = parser.getName();
                        if (name.equalsIgnoreCase("cell")){
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
