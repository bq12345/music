package org.bq;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.opensymphony.xwork2.ActionSupport;

/**
 * @author 白强
 * 
 */
public class Download extends ActionSupport {

	private String id;
	private String downFileName = "来自白强搜索的音乐";

	public String getDownFileName() {
		return downFileName;
	}

	public void setDownFileName(String downFileName) {
		this.downFileName = downFileName;
	}

	public InputStream getInputStream() throws Exception {
		URL url = new URL("http://www.xiami.com/widget/xml-single/sid/" + id);
		InputStream is = url.openStream();
		String location = parseXML(is);
		this.downFileName = new String(this.downFileName.getBytes("UTF-8"),
				"ISO-8859-1");
		URL address = new URL(getLocation(location));
		InputStream inputStream = address.openStream();
		return inputStream;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	private static final long serialVersionUID = 1L;

	public String getLocation(String location)
			throws UnsupportedEncodingException {
		int _local10;
		int _local2 = Integer.parseInt(location.substring(0, 1));
		String _local3 = location.substring(1, location.length());
		double _local4 = Math.floor(_local3.length() / _local2);
		int _local5 = _local3.length() % _local2;
		String[] _local6 = new String[_local2];
		int _local7 = 0;
		while (_local7 < _local5) {
			if (_local6[_local7] == null) {
				_local6[_local7] = "";
			}
			_local6[_local7] = _local3.substring(
					(((int) _local4 + 1) * _local7),
					(((int) _local4 + 1) * _local7) + ((int) _local4 + 1));
			_local7++;
		}
		_local7 = _local5;
		while (_local7 < _local2) {
			_local6[_local7] = _local3
					.substring(
							(((int) _local4 * (_local7 - _local5)) + (((int) _local4 + 1) * _local5)),
							(((int) _local4 * (_local7 - _local5)) + (((int) _local4 + 1) * _local5))
									+ (int) _local4);
			_local7++;
		}
		String _local8 = "";
		_local7 = 0;
		while (_local7 < ((String) _local6[0]).length()) {
			_local10 = 0;
			while (_local10 < _local6.length) {
				if (_local7 >= _local6[_local10].length()) {
					break;
				}
				_local8 = (_local8 + _local6[_local10].charAt(_local7));
				_local10++;
			}
			_local7++;
		}
		_local8 = URLDecoder.decode(_local8, "utf8");
		String _local9 = "";
		_local7 = 0;
		while (_local7 < _local8.length()) {
			if (_local8.charAt(_local7) == '^') {
				_local9 = (_local9 + "0");
			} else {
				_local9 = (_local9 + _local8.charAt(_local7));
			}
			;
			_local7++;
		}
		_local9 = _local9.replace("+", " ");
		return _local9;
	}

	@Override
	public String execute() throws Exception {
		return SUCCESS;
	}

	public String parseXML(InputStream is) {
		SAXReader xmlReader = new SAXReader();
		Document documnet = null;
		try {
			documnet = xmlReader.read(is);
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		Element root = documnet.getRootElement();
		Element track = root.element("track");
		Element location = track.element("location");
		Element name = track.element("song_name");
		downFileName = name.getText();
		return location.getText();
	}

}
