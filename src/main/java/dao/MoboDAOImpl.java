package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import javax.sql.DataSource;

import model.MoboBean;

public class MoboDAOImpl implements MoboDAO{
	private static final String TABLE_NAME = "mobo";

	private DataSource ds;

	public MoboDAOImpl(DataSource ds) {
	    this.ds = ds;
	}

	public synchronized void doSave(MoboBean mobo) throws SQLException {

	    ProdottoDAOImpl prodottoDAO = new ProdottoDAOImpl(ds);
	    prodottoDAO.doSave(mobo);

	    String sql = "INSERT INTO " + TABLE_NAME + " (chipset, socket, tiporam, maxfreq, formato, pcie, slotram, nvme, portesata, porteusb, fk_prodotto) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

	    try (Connection con = ds.getConnection();
	         PreparedStatement ps = con.prepareStatement(sql)) {

	        ps.setString(1, mobo.getChipset());
	        ps.setString(2, mobo.getSocket());
	        ps.setString(3, mobo.getTipoRam());
	        ps.setString(4, mobo.getMaxFreq());
	        ps.setString(5, mobo.getFormato());
	        ps.setString(6, mobo.getPcie());
	        ps.setInt(7, mobo.getSlotRam());
	        ps.setBoolean(8, mobo.isNvme());
	        ps.setInt(9, mobo.getPorteSata());
	        ps.setInt(10, mobo.getPorteUsb());
	        ps.setInt(11, mobo.getIdProdotto());

	        ps.executeUpdate();
	    }
	}

	public synchronized MoboBean doRetrieveByKey(int idMobo) throws SQLException {

	    String sql = "SELECT * FROM " + TABLE_NAME + " m JOIN prodotto p ON m.fk_prodotto = p.id_prodotto WHERE p.id_prodotto = ?";

	    try (Connection con = ds.getConnection();
	         PreparedStatement ps = con.prepareStatement(sql)) {

	        ps.setInt(1, idMobo);

	        ResultSet rs = ps.executeQuery();

	        if (rs.next()) {

	        		MoboBean mobo = new MoboBean();

	        			mobo.setIdProdotto(rs.getInt("id_prodotto"));
		            mobo.setNome(rs.getString("nome"));
		            mobo.setModello(rs.getString("modello"));
		            mobo.setDescrizione(rs.getString("descrizione"));
		            mobo.setMarca(rs.getString("marca"));
		            mobo.setPrezzo(rs.getDouble("prezzo"));
		            mobo.setStock(rs.getInt("stock"));
		            mobo.setDimensioni(rs.getString("dimensioni"));
                    mobo.setPeso(rs.getString("peso"));
		            mobo.setAttivo(rs.getBoolean("attivo"));
		            mobo.setSconto(rs.getInt("sconto"));
			        mobo.setCategoria(rs.getString("categoria"));

		            mobo.setIdMobo(rs.getInt("id_mobo"));
		            mobo.setChipset(rs.getString("chipset"));
		            mobo.setSocket(rs.getString("socket"));
		            mobo.setTipoRam(rs.getString("tiporam"));
		            mobo.setMaxFreq(rs.getString("maxfreq"));
		            mobo.setFormato(rs.getString("formato"));
		            mobo.setPcie(rs.getString("pcie"));
		            mobo.setSlotRam(rs.getInt("slotram"));
		            mobo.setNvme(rs.getBoolean("nvme"));
		            mobo.setPorteSata(rs.getInt("portesata"));
		            mobo.setPorteUsb(rs.getInt("porteusb"));

	            return mobo;
	        }
	    }

	    return null;
	}
	
	@Override
	public synchronized Collection<MoboBean> doRetrieveAll(String categoria, double prezzo, String marca, String formato, String nvme, int slotram) 
			throws SQLException {

	    List<MoboBean> lista = new LinkedList<>();

	    String sql =
	        "SELECT * " +
	        "FROM mobo m " +
	        "JOIN prodotto p ON m.fk_prodotto = p.id_prodotto " +
	        "WHERE (? IS NULL OR p.categoria = ?) " +
	        "AND (? IS NULL OR p.marca = ?) " +
	        "AND (? IS NULL OR p.prezzo <= ?) " +
	        "AND (? IS NULL OR m.formato = ?) " +
	        "AND (? IS NULL OR m.nvme = ?) " +
	        "AND (? = 0 OR m.slotram = ?)";

	    try (Connection con = ds.getConnection();
	         PreparedStatement ps = con.prepareStatement(sql)) {

	        ps.setString(1, categoria);
	        ps.setString(2, categoria);

	        ps.setString(3, marca);
	        ps.setString(4, marca);

	        ps.setDouble(5, prezzo);
	        ps.setDouble(6, prezzo);

	        ps.setString(7, formato);
	        ps.setString(8, formato);

	        ps.setString(9, nvme);
	        ps.setString(10, nvme);

	        ps.setInt(11, slotram);
	        ps.setInt(12, slotram);

	        try (ResultSet rs = ps.executeQuery()) {

	            while (rs.next()) {

	                MoboBean mobo = new MoboBean();

	                mobo.setIdProdotto(rs.getInt("id_prodotto"));
		            mobo.setNome(rs.getString("nome"));
		            mobo.setModello(rs.getString("modello"));
		            mobo.setDescrizione(rs.getString("descrizione"));
		            mobo.setMarca(rs.getString("marca"));
		            mobo.setPrezzo(rs.getDouble("prezzo"));
		            mobo.setStock(rs.getInt("stock"));
		            mobo.setDimensioni(rs.getString("dimensioni"));
                    mobo.setPeso(rs.getString("peso"));
		            mobo.setAttivo(rs.getBoolean("attivo"));
		            mobo.setSconto(rs.getInt("sconto"));
			        mobo.setCategoria(rs.getString("categoria"));

		            mobo.setIdMobo(rs.getInt("id_mobo"));
		            mobo.setChipset(rs.getString("chipset"));
		            mobo.setSocket(rs.getString("socket"));
		            mobo.setTipoRam(rs.getString("tiporam"));
		            mobo.setMaxFreq(rs.getString("maxfreq"));
		            mobo.setFormato(rs.getString("formato"));
		            mobo.setPcie(rs.getString("pcie"));
		            mobo.setSlotRam(rs.getInt("slotram"));
		            mobo.setNvme(rs.getBoolean("nvme"));
		            mobo.setPorteSata(rs.getInt("portesata"));
		            mobo.setPorteUsb(rs.getInt("porteusb"));

	                lista.add(mobo);
	            }
	        }
	    }

	    return lista;
	}

	public synchronized Collection<MoboBean> doRetrieveAll() throws SQLException {

	    List<MoboBean> lista = new LinkedList<>();

	    String sql = "SELECT * FROM " + TABLE_NAME + " m JOIN prodotto p ON m.fk_prodotto = p.id_prodotto";

	    try (Connection con = ds.getConnection();
	         PreparedStatement ps = con.prepareStatement(sql);
	         ResultSet rs = ps.executeQuery()) {

	        while (rs.next()) {

	        	    MoboBean mobo = new MoboBean();

	            mobo.setIdProdotto(rs.getInt("id_prodotto"));
	            mobo.setNome(rs.getString("nome"));
	            mobo.setModello(rs.getString("modello"));
	            mobo.setDescrizione(rs.getString("descrizione"));
	            mobo.setMarca(rs.getString("marca"));
	            mobo.setPrezzo(rs.getDouble("prezzo"));
	            mobo.setStock(rs.getInt("stock"));
	            mobo.setAttivo(rs.getBoolean("attivo"));
	            mobo.setSconto(rs.getInt("sconto"));
		        mobo.setCategoria(rs.getString("categoria"));

	            mobo.setIdMobo(rs.getInt("id_mobo"));
	            mobo.setChipset(rs.getString("chipset"));
	            mobo.setSocket(rs.getString("socket"));
	            mobo.setTipoRam(rs.getString("tiporam"));
	            mobo.setMaxFreq(rs.getString("maxfreq"));
	            mobo.setFormato(rs.getString("formato"));
	            mobo.setPcie(rs.getString("pcie"));
	            mobo.setSlotRam(rs.getInt("slotram"));
	            mobo.setNvme(rs.getBoolean("nvme"));
	            mobo.setPorteSata(rs.getInt("portesata"));
	            mobo.setPorteUsb(rs.getInt("porteusb"));

	            lista.add(mobo);
	        }
	    }

	    return lista;
	}

	public synchronized boolean doUpdate(MoboBean mobo) throws SQLException {

	    ProdottoDAOImpl prodottoDAO = new ProdottoDAOImpl(ds);
	    prodottoDAO.doUpdate(mobo);

	    String sql = "UPDATE " + TABLE_NAME + " SET chipset = ?, socket = ?, tiporam = ?, maxfreq = ?, formato = ?, pcie = ?, slotram = ?, nvme = ?, portesata = ?, porteusb = ? WHERE id_mobo = ?";

	    try (Connection con = ds.getConnection();
	         PreparedStatement ps = con.prepareStatement(sql)) {

	        ps.setString(1, mobo.getChipset());
	        ps.setString(2, mobo.getSocket());
	        ps.setString(3, mobo.getTipoRam());
	        ps.setString(4, mobo.getMaxFreq());
	        ps.setString(5, mobo.getFormato());
	        ps.setString(6, mobo.getPcie());
	        ps.setInt(7, mobo.getSlotRam());
	        ps.setBoolean(8, mobo.isNvme());
	        ps.setInt(9, mobo.getPorteSata());
	        ps.setInt(10, mobo.getPorteUsb());
	        ps.setInt(11, mobo.getIdMobo());

	        return ps.executeUpdate() > 0;
	    }
	}

	public synchronized boolean setProductStatus(MoboBean mobo, boolean attivo) throws SQLException {

	    ProdottoDAOImpl prodottoDAO = new ProdottoDAOImpl(ds);

	    return prodottoDAO.setProductStatus(mobo.getIdProdotto(), attivo);
	}
}
