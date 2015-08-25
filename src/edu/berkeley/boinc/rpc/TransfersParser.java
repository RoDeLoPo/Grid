/*******************************************************************************
 * This file is part of BOINC. http://boinc.berkeley.edu Copyright (C) 2012
 * University of California
 * 
 * BOINC is free software; you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 * 
 * BOINC is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with BOINC. If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/

package edu.berkeley.boinc.rpc;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class TransfersParser extends BaseParser {
	
	private ArrayList<Transfer> mTransfers = new ArrayList<Transfer>();
	private Transfer mTransfer = null;
	
	public final ArrayList<Transfer> getTransfers() {
		return mTransfers;
	}
	
	/**
	 * Parse the RPC result (projects) and generate vector of projects info
	 * 
	 * @param rpcResult
	 *             String returned by RPC call of core client
	 * @return vector of projects info
	 */
	public static ArrayList<Transfer> parse(String rpcResult) {
		TransfersParser parser = new TransfersParser();
		try {
			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser saxParser = factory.newSAXParser();
			saxParser.parse(new InputSource(new StringReader(rpcResult)), parser);
		} catch(SAXException e) {
			return null;
		} catch(ParserConfigurationException e) {
			e.printStackTrace();
		} catch(IOException e) {
			e.printStackTrace();
		}
		return parser.getTransfers();
	}
	
	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		super.startElement(uri, localName, qName, attributes);
		if(qName.equalsIgnoreCase("file_transfer")) {
			mTransfer = new Transfer();
		} else if(qName.equalsIgnoreCase("file_xfer")) {
			// Just constructor, flag should be set if it's present
			if(mTransfer != null) {
				mTransfer.xfer_active = true;
			}
		} else if(qName.equalsIgnoreCase("persistent_file_xfer")) {
			// Just constructor, but nothing to do here
			// We just do not set mElementStarted flag here, so we will
			// avoid unnecessary work in BaseParser.characters()
		} else {
			// Another element, hopefully primitive and not constructor
			// (although unknown constructor does not hurt, because there
			// will be primitive start anyway)
			mElementStarted = true;
			mCurrentElement.setLength(0);
		}
	}
	
	// Method characters(char[] ch, int start, int length) is implemented by
	// BaseParser,
	// filling mCurrentElement (including stripping of leading whitespaces)
	// @Override
	// public void characters(char[] ch, int start, int length) throws
	// SAXException { }
	
	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		super.endElement(uri, localName, qName);
		try {
			if(mTransfer != null) {
				// We are inside <file_transfer>
				if(qName.equalsIgnoreCase("file_transfer")) {
					// Closing tag of <project> - add to vector and be
					// ready for next one
					if(!mTransfer.project_url.equals("") && !mTransfer.name.equals("")) {
						// project_url is a must
						mTransfers.add(mTransfer);
					}
					mTransfer = null;
				} else {
					// Not the closing tag - we decode possible inner tags
					trimEnd();
					if(qName.equalsIgnoreCase("project_url")) {
						mTransfer.project_url = mCurrentElement.toString();
					} else if(qName.equalsIgnoreCase("name")) {
						mTransfer.name = mCurrentElement.toString();
					} else if(qName.equalsIgnoreCase("generated_locally")) {
						mTransfer.generated_locally = !mCurrentElement.toString().equals("0");
					} else if(qName.equalsIgnoreCase("is_upload")) {
						mTransfer.is_upload = !mCurrentElement.toString().equals("0");
					} else if(qName.equalsIgnoreCase("nbytes")) {
						mTransfer.nbytes = (long)Double.parseDouble(mCurrentElement.toString());
					} else if(qName.equalsIgnoreCase("status")) {
						mTransfer.status = Integer.parseInt(mCurrentElement.toString());
					} else if(qName.equalsIgnoreCase("time_so_far")) {
						// inside <persistent_file_xfer>
						mTransfer.time_so_far = (long)Double.parseDouble(mCurrentElement.toString());
					} else if(qName.equalsIgnoreCase("next_request_time")) {
						// inside <persistent_file_xfer>
						mTransfer.next_request_time = (long)Double.parseDouble(mCurrentElement.toString());
					} else if(qName.equalsIgnoreCase("last_bytes_xferred")) {
						// inside <persistent_file_xfer>
						// See also <bytes_xferred> below, both are
						// setting the same parameters
						if(mTransfer.bytes_xferred == 0) {
							// Not set yet
							mTransfer.bytes_xferred = (long)Double.parseDouble(mCurrentElement.toString());
						}
					} else if(qName.equalsIgnoreCase("bytes_xferred")) {
						// Total bytes transferred, but this info is not
						// available if networking
						// is suspended. This info is present only inside
						// <file_xfer> (active transfer)
						// In such case we overwrite value set by
						// <last_bytes_xferred>
						mTransfer.bytes_xferred = (long)Double.parseDouble(mCurrentElement.toString());
					} else if(qName.equalsIgnoreCase("xfer_speed")) {
						// inside <file_xfer>
						mTransfer.xfer_speed = Float.parseFloat(mCurrentElement.toString());
					} else if(qName.equalsIgnoreCase("project_backoff")) {
						mTransfer.project_backoff = (long)Double.parseDouble(mCurrentElement.toString());
					}
				}
			}
		} catch(NumberFormatException e) {
		}
		mElementStarted = false;
	}
}