package me.zuif.hw18;


import me.zuif.hw18.request.Request;
import me.zuif.hw18.request.RequestRepository;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;

@WebServlet("/hw18")
public class MainServlet extends HttpServlet {
    private final RequestRepository REQUEST_REPOSITORY = RequestRepository.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String ipAddress = req.getRemoteAddr();
        String agent = req.getHeader("user-agent");
        Request thisRequest = new Request(ipAddress, agent, LocalDateTime.now());
        REQUEST_REPOSITORY.add(thisRequest);
        req.setAttribute("repo", REQUEST_REPOSITORY.findAll());
        req.getRequestDispatcher("index.jsp").forward(req, resp);
    }


}