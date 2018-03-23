package com.buptyouth.mybatis.model;

import java.util.Hashtable;

import javax.naming.AuthenticationException;
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.Control;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;

public class LDAPAuthentication {

    private final String ldapFactory = "com.sun.jndi.ldap.LdapCtxFactory";
    private final String ldapUrl = "ldap://10.3.55.43:389/";// url
    private final String ldapAccount = "xtw"; // 用户名
    private final String ldapPwd = "xtw123456";//密码
    private final String root = "dc=bupt,dc=edu,dc=cn"; // root
    private LdapContext ctx = null;
    private Control[] connCtls = null;
    private SearchResult searchResult;

    /**
     * 连接LDAP
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public void connetLDAP() throws NamingException {
        // 连接Ldap需要的信息

        Hashtable env = new Hashtable();
        env.put(Context.INITIAL_CONTEXT_FACTORY, ldapFactory);
        // LDAP server
        env.put(Context.PROVIDER_URL, ldapUrl + root);
        env.put(Context.SECURITY_AUTHENTICATION, "simple");
        env.put(Context.SECURITY_PRINCIPAL, "uid=" + ldapAccount + ",ou=Manager,dc=bupt,dc=edu,dc=cn");
        env.put(Context.SECURITY_CREDENTIALS, ldapPwd);
        env.put("java.naming.referral", "follow");
        ctx = new InitialLdapContext(env, connCtls);
    }

    private String getUserDN(String uid) throws NamingException {
        String userDN = "";
        connetLDAP();
        try {
            SearchControls constraints = new SearchControls();
            constraints.setSearchScope(SearchControls.SUBTREE_SCOPE);
            NamingEnumeration<SearchResult> en = ctx.search("", "uid=" + uid, constraints);
            //System.out.println(uid);
            if (en == null || !en.hasMoreElements()) {
                System.out.println("未找到该用户");
            }
            // maybe more than one element
            while (en != null && en.hasMoreElements()) {
                Object obj = en.nextElement();
                if (obj instanceof SearchResult) {
                    SearchResult si = (SearchResult) obj;
                    userDN += si.getName();
                    userDN += "," + root;
                    searchResult = si;
                } else {
                    System.out.println(obj);
                }
            }
        } catch (Exception e) {
            System.out.println("查找用户时产生异常。");
            e.printStackTrace();
        }

        return userDN;
    }

    public String authenricate(String UID, String password) throws NamingException {
        String username = null;
        boolean valide = false;
        String userDN = getUserDN(UID);

        try {
            ctx.addToEnvironment(Context.SECURITY_PRINCIPAL, userDN);
            ctx.addToEnvironment(Context.SECURITY_CREDENTIALS, password);
            ctx.reconnect(connCtls);
            String longName = (String) searchResult.getAttributes().get("CN").get();
            username = longName.split("\\d+")[0];
            System.out.println(username);
            System.out.println(userDN + " 验证通过");
            return username;
        } catch (AuthenticationException e) {
            System.out.println(userDN + " 验证失败1");
            System.out.println(e.toString());
        } catch (NamingException e) {
            System.out.println(userDN + " 验证失败2");
        }

        return null;
    }
}
