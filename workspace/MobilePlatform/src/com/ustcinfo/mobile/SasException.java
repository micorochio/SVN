/**
* ������: 	SasMobile
* �ļ���: 	SasException.java
* ������:  	ycxiong
* ����ʱ��: 	2011-7-11 ����03:13:51
* ��Ȩ���У�	Copyright (c) 2011 ���ݿƴ������Ϣ�������޹�˾  
* �ļ�����: �������ļ�������
* -----------------------------����¼ ----------------------------- 
* ����        		�����      		�汾��  		�������  
* ------------------------------------------------------------------  
* 2011-7-11     ycxiong   	1.0.0       	first created  
*/
package com.ustcinfo.mobile;

/**
 * @since SasMobile 1.0.0
 * @version 1.0 2011-7-11
 * @author ycxiong
 */
public class SasException extends RuntimeException {

	private static final long serialVersionUID = -8142487232572986928L;

	/**
	 * 
	 */
	public SasException() {
	}

	/**
	 * @param detailMessage
	 */
	public SasException(String detailMessage) {
		super(detailMessage);
	}

	/**
	 * @param throwable
	 */
	public SasException(Throwable throwable) {
		super(throwable);
	}

	/**
	 * @param detailMessage
	 * @param throwable
	 */
	public SasException(String detailMessage, Throwable throwable) {
		super(detailMessage, throwable);
	}

}
