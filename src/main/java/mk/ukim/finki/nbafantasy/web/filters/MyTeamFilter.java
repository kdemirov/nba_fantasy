package mk.ukim.finki.nbafantasy.web.filters;

import mk.ukim.finki.nbafantasy.model.User;
import mk.ukim.finki.nbafantasy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@WebFilter
public class MyTeamFilter implements Filter {

    private final UserService userService;

    public MyTeamFilter(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request=(HttpServletRequest) servletRequest;
        HttpServletResponse response=(HttpServletResponse) servletResponse;
        User user=null;
        try {
            user = this.userService.findByUsername(request.getRemoteUser());
        }catch (UsernameNotFoundException e){
            System.out.println(e.getMessage());
        }
        String path=request.getServletPath();
        if(user!=null&&user.getMyTeam().size()<5&&path.equals("/myteam")){
            response.sendRedirect("/transfers");
        }else{
            filterChain.doFilter(servletRequest,servletResponse);
        }
    }
}
