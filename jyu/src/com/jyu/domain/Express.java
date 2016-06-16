package com.jyu.domain;

import java.io.Serializable;

public class Express implements Serializable{

	/**
	 * 关于myeclips提示The serializable class XXX does not declare a static final serialVersionUID field of type long的警告
我们在用eclips/myeclips的时候，会出现这个warning，比如在用hibernate时，自动生成表的对应类后，就有这个提示。这是为什么呢？
这与jdk的版本没关系，那是Eclipse提供的功能.
你点它warning的icon两下Eclipse就会自动给定，如果你不喜欢,可以把它关掉,
windows -> preferences -> compiler -> Error/Warnings -> Potential Programming problems
将Serializable class without serialVersionUID的warning改成ignore.

其实如果你没有考虑到兼容性问题时,那就把它关掉吧.
其实有这个功能是好的.只要任何类别实作了Serializable这个介面,
如果没有加入serialVersionUID,Eclipse都会给你warning提示,
这个serialVersionUID为了让该类别Serializable後兼容.

考虑一下,如果今天你的类Serialized存到硬碟里,
可是後来你却更改了类别的field(增加或减少或改名).
当你Deserialize时,就会出现Exception.这样就会做成不兼容性的问题.

但当serialVersionUID相同时,它就会将不一样的field以type的预设值Deserialize.
这个可以避开不兼容性的问题.
	 */
	private static final long serialVersionUID = 2365582887211393438L;
	private String time;
	private String context;
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getContext() {
		return context;
	}
	public void setContext(String context) {
		this.context = context;
	}
	
	
}
