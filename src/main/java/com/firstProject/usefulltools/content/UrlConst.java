package com.firstProject.usefulltools.content;

public class UrlConst {

    /**ログイン画面 */
    public static final String LOGIN = "/usefulltools/login";

    /**ユーザー登録画面 */
    public static final String REGISTER = "/usefulltools/register";

        /**ユーザー登録画面 */
    public static final String REGISTERS = "/usefulltools/register-s";

    /**Top page*/
    public static final String WORKOUTTOP = "/usefulltools/content-work-out-top";

    /**ログイン画面を返すとき */
    public static final String LOGINS = "/usefulltools/login-s";

    /**work out */
    public static final String WORKOUT = "/usefulltools/content-work-out-schedule";

    /**パスワード変更画面 */
    public static final String REGISTERCHANGE = "/usefulltools/register-change";

    public static final String help = "/usefulltools/help";

    public static final String AccountDelete = "/usefulltools/AccountDelete";

    /**ログイン画面 */
    public static final String [] NO_AUTHENTICATION = {REGISTER,REGISTERS,LOGIN,
        WORKOUTTOP,WORKOUT,LOGINS,REGISTERCHANGE,help,AccountDelete,"/css/**", "/js/**","/images/**",} ;
    


    
}
