package org.crossmobile.source.xtype;

/**
 * This class abstracts the advice from the 'code' tag of the advisor. It
 * maintains the code snippet along with the mode which can be either of the
 * three values: 'replace' - which would just copy the entire code snippet
 * within the wrapper comments 'before' - which adds the code snippet above the
 * generated code within the wrapper comments 'after' - which adds the code
 * snippet below the generated code within the wrapper comments
 * 
 */
public class XCode {

    protected String code;
    protected String mode;
    protected String language;


    public XCode(String code, String language, String mode) {
        this.code = code;
        this.mode = mode;
        this.language = language;

    }

    public String getCodelanguage() {
        return this.language;
    }

    /**
     * Returns the code snippet as given in the advisor
     * 
     * @return - code snippet provided in 'code' tag in advisor
     */
    public String getCode() {
        return this.code;
    }

    /**
     * Returns the value of mode which can be either 'replace' 'before' or
     * 'after' based on which the code injection is done.
     * 
     * @return - value of mode specified by advisor
     */
    public String getMode() {
        return this.mode;
    }
}
