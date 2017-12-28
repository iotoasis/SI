package net.herit.iot.onem2m.core.util;

public class StackTraceInfo {

	private static final int CLIENT_CODE_STACK_INDEX;

	static {
		// Finds out the index of "this code" in the returned stack trace - funny but it differs in JDK 1.5 and 1.6
		int i = 0;
		for (StackTraceElement ste: Thread.currentThread().getStackTrace()) {
			i++;
			if (ste.getClassName().equals(StackTraceInfo.class.getName())) {
				break;
			}
		}
		CLIENT_CODE_STACK_INDEX = i;
	}

	public String getCurrentMethodName() {
		return getCurrentMethodName(1);     // making additional overloaded method call requires +1 offset
	}

	private String getCurrentMethodName(int offset) {
		return Thread.currentThread().getStackTrace()[CLIENT_CODE_STACK_INDEX + offset].getMethodName();
	}

	public String getCurrentClassName() {
		return getCurrentClassName(1);      // making additional overloaded method call requires +1 offset
	}

	private String getCurrentClassName(int offset) {
		return Thread.currentThread().getStackTrace()[CLIENT_CODE_STACK_INDEX + offset].getClassName();
	}

	public String getCurrentFileName() {
		return getCurrentFileName(1);     // making additional overloaded method call requires +1 offset
	}

	private String getCurrentFileName(int offset) {
		String filename = Thread.currentThread().getStackTrace()[CLIENT_CODE_STACK_INDEX + offset].getFileName();
		int lineNumber = Thread.currentThread().getStackTrace()[CLIENT_CODE_STACK_INDEX + offset].getLineNumber();

		return filename + ":" + lineNumber;
	}

	public String getInvokingMethodName() {
		return getInvokingMethodName(2); 
	}

	private String getInvokingMethodName(int offset) {
		return getCurrentMethodName(offset + 1);    // re-uses getCurrentMethodName() with desired index
	}

	public String getInvokingClassName() {
		return getInvokingClassName(2); 
	}

	private String getInvokingClassName(int offset)	{
		return getCurrentClassName(offset + 1);     // re-uses getCurrentClassName() with desired index
	}

	public String getInvokingFileName() {
		return getInvokingFileName(2); 
	}

	private String getInvokingFileName(int offset) {
		return getCurrentFileName(offset + 1);     // re-uses getCurrentFileName() with desired index
	}

	public String getCurrentMethodNameFqn() {
		return getCurrentMethodNameFqn(1);
	}

	private String getCurrentMethodNameFqn(int offset) {
		String currentClassName = getCurrentClassName(offset + 1);
		String currentMethodName = getCurrentMethodName(offset + 1);

		return currentClassName + "." + currentMethodName ;
	}

	public String getCurrentFileNameFqn() {
		String CurrentMethodNameFqn = getCurrentMethodNameFqn(1);
		String currentFileName = getCurrentFileName(1);

		return CurrentMethodNameFqn + "(" + currentFileName + ")";
	}

	public String getInvokingMethodNameFqn() {
		return getInvokingMethodNameFqn(2);
	}

	private String getInvokingMethodNameFqn(int offset) {
		String invokingClassName = getInvokingClassName(offset + 1);
		String invokingMethodName = getInvokingMethodName(offset + 1);

		return invokingClassName + "." + invokingMethodName;
	}

	public String getInvokingFileNameFqn() {
		String invokingMethodNameFqn = getInvokingMethodNameFqn(2);
		String invokingFileName = getInvokingFileName(2);

		return invokingMethodNameFqn + "(" + invokingFileName + ")";
	}
}