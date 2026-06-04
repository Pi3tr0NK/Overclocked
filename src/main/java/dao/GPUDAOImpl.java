package dao;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import model.GPUBean;

public class GPUDAOImpl implements GPUDAO {

	private static final String TABLE_NAME = "gpu";
    private DataSource ds = null;
    
    public GPUDAOImpl(DataSource ds) {
        this.ds = ds;
    }
    
	public synchronized void doSave(GPUBean gpu) throws SQLException {
        
		ProdottoDAOImpl prodottoDAO = new ProdottoDAOImpl(ds);
        prodottoDAO.doSave(gpu);

        String sql = "INSERT INTO "+ TABLE_NAME +" (frequenza, vram, video, tipovram, pcie, maxres, tdp, fk_prodotto) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = ds.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, gpu.getFrequenza());
            ps.setString(2, gpu.getVram());
            ps.setString(3, gpu.getVideo());
            ps.setString(4, gpu.getTipoVram());
            ps.setString(5, gpu.getPcie());
            ps.setString(6, gpu.getMaxRes());
            ps.setInt(6, gpu.getTdp());
            ps.setInt(7, gpu.getIdProdotto());
            ps.executeUpdate();
        }
    }
	
	public GPUBean doRetrieveByKey (int idGPU) throws SQLException {
		
        String sql = "SELECT * FROM "+TABLE_NAME+" g "
        		+ "JOIN prodotto p ON g.fk_prodotto = p.id_prodotto "
        		+ "WHERE p.id_prodotto = ?";

        try (Connection con = ds.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idGPU);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                GPUBean gpu = new GPUBean();
                gpu.setIdProdotto(rs.getInt("id_prodotto"));
                gpu.setNome(rs.getString("nome"));
                gpu.setModello(rs.getString("modello"));
                gpu.setDescrizione(rs.getString("descrizione"));
                gpu.setMarca(rs.getString("marca"));
                gpu.setPrezzo(rs.getDouble("prezzo"));
                gpu.setStock(rs.getInt("stock"));
                gpu.setDimensioni(rs.getString("dimensioni"));
                gpu.setPeso(rs.getString("peso"));
                gpu.setAttivo(rs.getBoolean("attivo"));
                gpu.setSconto(rs.getInt("sconto"));
                gpu.setCategoria(rs.getString("categoria"));
                
                gpu.setIdGpu(rs.getInt("id_gpu"));
                gpu.setFrequenza(rs.getString("frequenza"));
                gpu.setVram(rs.getString("vram"));
                gpu.setVideo(rs.getString("video"));
                gpu.setTipoVram(rs.getString("tipovram"));
                gpu.setPcie(rs.getString("pcie"));
                gpu.setMaxRes(rs.getString("maxres"));
                gpu.setTdp(rs.getInt("tdp"));
                
                return gpu;
            }
        }
        return null;
	}
	
	@Override
	public synchronized List<GPUBean> doRetrieveAll(String categoria, String prezzo, String marca, String vram, String pcie) 
			throws SQLException {

	    List<GPUBean> lista = new LinkedList<>();
	    
	    categoria = (categoria == null || categoria.trim().isEmpty()) ? null : categoria;
	    marca = (marca == null || marca.trim().isEmpty()) ? null : marca;
	    vram = (vram == null || vram.trim().isEmpty()) ? null : vram;
	    pcie = (pcie == null || pcie.trim().isEmpty()) ? null : pcie;

	    Double prezzoMax = null;
	    if (prezzo != null && !prezzo.trim().isEmpty()) {
	        prezzoMax = Double.parseDouble(prezzo);
	    }

	    String sql =
	        "SELECT * " +
	        "FROM gpu g " +
	        "JOIN prodotto p ON g.fk_prodotto = p.id_prodotto " +
	        "WHERE (? IS NULL OR p.categoria = ?) " +
	        "AND (? IS NULL OR p.marca = ?) " +
	        "AND (? IS NULL OR p.prezzo <= ?) " +
	        "AND (? IS NULL OR g.vram = ?) " +
	        "AND (? IS NULL OR g.pcie = ?)";

	    try (Connection con = ds.getConnection();
	         PreparedStatement ps = con.prepareStatement(sql)) {

	        ps.setString(1, categoria);
	        ps.setString(2, categoria);

	        ps.setString(3, marca);
	        ps.setString(4, marca);

	        if (prezzoMax == null) {
	            ps.setNull(5, java.sql.Types.DOUBLE);
	            ps.setNull(6, java.sql.Types.DOUBLE);
	        } else {
	            ps.setDouble(5, prezzoMax);
	            ps.setDouble(6, prezzoMax);
	        }

	        ps.setString(7, vram);
	        ps.setString(8, vram);

	        ps.setString(9, pcie);
	        ps.setString(10, pcie);

	        try (ResultSet rs = ps.executeQuery()) {

	            while (rs.next()) {

	                GPUBean gpu = new GPUBean();

	                gpu.setIdProdotto(rs.getInt("id_prodotto"));
	                gpu.setNome(rs.getString("nome"));
	                gpu.setModello(rs.getString("modello"));
	                gpu.setDescrizione(rs.getString("descrizione"));
	                gpu.setMarca(rs.getString("marca"));
	                gpu.setPrezzo(rs.getDouble("prezzo"));
	                gpu.setStock(rs.getInt("stock"));
	                gpu.setDimensioni(rs.getString("dimensioni"));
	                gpu.setPeso(rs.getString("peso"));
	                gpu.setAttivo(rs.getBoolean("attivo"));
	                gpu.setSconto(rs.getInt("sconto"));
	                gpu.setCategoria(rs.getString("categoria"));
	                
	                gpu.setIdGpu(rs.getInt("id_gpu"));
	                gpu.setFrequenza(rs.getString("frequenza"));
	                gpu.setVram(rs.getString("vram"));
	                gpu.setVideo(rs.getString("video"));
	                gpu.setTipoVram(rs.getString("tipovram"));
	                gpu.setPcie(rs.getString("pcie"));
	                gpu.setMaxRes(rs.getString("maxres"));
	                gpu.setTdp(rs.getInt("tdp"));

	                lista.add(gpu);
	            }
	        }
	    }

	    return lista;
	}
    
	public synchronized boolean doUpdate(GPUBean gpu) throws SQLException
	{
		ProdottoDAOImpl prodottoDAO = new ProdottoDAOImpl(ds);
		prodottoDAO.doUpdate(gpu);
		
        String sql = "UPDATE " + TABLE_NAME + " SET frequenza=?, vram=?, video=?, tipovram=?, pcie=?, maxres=?, tdp=? WHERE id_gpu=?";

        try(Connection con = ds.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, gpu.getFrequenza());
            ps.setString(2, gpu.getVram());
            ps.setString(3, gpu.getVideo());
            ps.setString(4, gpu.getTipoVram());
            ps.setString(5, gpu.getPcie());
            ps.setString(6, gpu.getMaxRes());
            ps.setInt(7, gpu.getTdp());
            ps.setInt(8, gpu.getIdGpu());

            return ps.executeUpdate() > 0;
        }
	}
	
    public synchronized boolean setProductStatus(GPUBean gpu, boolean attivo) throws SQLException {

    	ProdottoDAOImpl prodottoDAO = new ProdottoDAOImpl(ds);
    	
    	return prodottoDAO.setProductStatus(gpu.getIdProdotto(),attivo);


    }
 }
 

