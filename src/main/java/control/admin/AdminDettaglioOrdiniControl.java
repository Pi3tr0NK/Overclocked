package control.admin;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;

import javax.sql.DataSource;

import dao.DettaglioOrdineDAOImpl;
import dao.OrdineDAOImpl;

/**
 * Servlet implementation class AdminDettaglioOrdiniControl
 */
@WebServlet("/admin/dettaglioOrdini")
public class AdminDettaglioOrdiniControl extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
       
    private DataSource ds;
    private OrdineDAOImpl ordineDAO;
    private DettaglioOrdineDAOImpl dettagliOrdineDAO;
	   @Override
	   public void init(ServletConfig config) throws ServletException {

		   super.init(config);
		        
		   ds = (DataSource) getServletContext().getAttribute("DataSource");
	
		   if(ds == null) {
			   throw new ServletException("DataSource non disponibile");
		   }
		   
		   ordineDAO = new OrdineDAOImpl(ds);
		   dettagliOrdineDAO = new DettaglioOrdineDAOImpl(ds);
	   }


	 
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		
	
        String idOrdineStr = request.getParameter("idOrdine");
        
        int idOrdine = Integer.valueOf(idOrdineStr);
        
        ottieniOrdine(request, idOrdine);
        ottieniDettagliOrdine(request, idOrdine);

        
		request.getRequestDispatcher("/WEB-INF/views/admin/DettaglioOrdineView.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
	
	private void ottieniOrdine(HttpServletRequest request, int idOrdine)
	{
        try {
			request.setAttribute("ordine",ordineDAO.doRetrieveByKey(idOrdine));
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private void ottieniDettagliOrdine(HttpServletRequest request, int idOrdine)
	{
		
		
        try {
			request.setAttribute("dettagli",dettagliOrdineDAO.doRetrieveByOrdine(idOrdine));
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	
	
	
	
	
	

}
