package controller;

import utils.MessageUtils;
import utils.MyUtils;

import com.jfinal.core.Controller;


public class SendController extends Controller {
	
	public void index() throws Exception{
//		MyUtils.send();
		MyUtils.sendNativeEmail();
		render("index.jsp");
	}
	
}
