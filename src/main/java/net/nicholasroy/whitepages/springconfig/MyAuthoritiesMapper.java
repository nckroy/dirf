//package net.nicholasroy.springtest2.springconfig;
//
//import java.util.Collection;
//import java.util.EnumSet;
//import java.util.Set;
//
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
//
//enum MyAuthority implements GrantedAuthority {
//    ROLE_ADMIN,
//    ROLE_USER;
//
//    public String getAuthority() {
//        return name();
//    }
//}
//
//
//class MyAuthoritiesMapper implements GrantedAuthoritiesMapper {
//
//    public Collection<? extends GrantedAuthority> mapAuthorities(Collection<? extends GrantedAuthority> authorities) {
//        Set<MyAuthority> roles = EnumSet.noneOf(MyAuthority.class);
//
//        for (GrantedAuthority a: authorities) {
//            if ("cn=dn-of-admin-group".equals(a.getAuthority())) {
//                roles.add(MyAuthority.ROLE_ADMIN);
//            } else if ("cn=dn-of-user-group".equals(a.getAuthority())) {
//                roles.add(MyAuthority.ROLE_USER);
//            }
//        }
//
//        return roles;
//    }
//}