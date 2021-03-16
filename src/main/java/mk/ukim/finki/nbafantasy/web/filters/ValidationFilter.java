package mk.ukim.finki.nbafantasy.web.filters;

import mk.ukim.finki.nbafantasy.model.User;
import mk.ukim.finki.nbafantasy.service.UserService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter
public class ValidationFilter implements Filter {


    private final UserService userService;

    public ValidationFilter(UserService userService) {
        this.userService = userService;
    }


    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request=(HttpServletRequest) servletRequest;
        HttpServletResponse response=(HttpServletResponse) servletResponse;
        String path=request.getServletPath();
        User user=null;
        try {
             user = this.userService.findByUsername(request.getRemoteUser());
        }catch (UsernameNotFoundException e){
            System.out.println(e.getMessage());
        }
        if(user!=null&&!user.isEnabled()&&(path.equals("/myteam")||path.equals("/transfers"))){
            response.sendRedirect("/verify-account");
        }else{
            filterChain.doFilter(servletRequest,servletResponse);
        }
    }
}
