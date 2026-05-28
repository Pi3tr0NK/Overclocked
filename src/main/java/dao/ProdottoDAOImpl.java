package dao;

import java.sql.*;
import java.util.*;

import javax.sql.DataSource;

import model.ProdottoBean;

public class ProdottoDAOImpl implements ProdottoDAO{
	private static final String TABLE_NAME = "prodotto";
    private DataSource ds = null;
    
    public ProdottoDAOImpl(DataSource ds) {
        this.ds = ds;
    }
	
    public synchronized void doSave(ProdottoBean p) throws SQLException {
        String sql = "INSERT INTO " + TABLE_NAME + " (nome, modello, descrizione, marca, prezzo, stock, attivo, sconto) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = ds.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, p.getNome());
            ps.setString(2, p.getModello());
            ps.setString(3, p.getDescrizione());
            ps.setString(4, p.getMarca());
            ps.setDouble(5, p.getPrezzo());
            ps.setInt(6, p.getStock());
            ps.setBoolean(7, p.isAttivo());
            ps.setInt(8, p.getSconto());
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    int idGenerato = rs.getInt(1);
                    p.setIdProdotto(idGenerato);
                }
            }
        }
    }

    public synchronized ProdottoBean doRetrieveByKey(int id) throws SQLException {
        String sql = "SELECT * FROM "+TABLE_NAME+" WHERE id_prodotto = ?";

        try (Connection con = ds.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                ProdottoBean p = new ProdottoBean();
                p.setIdProdotto(rs.getInt("id_prodotto"));
                p.setNome(rs.getString("nome"));
                p.setModello(rs.getString("modello"));
                p.setDescrizione(rs.getString("descrizione"));
                p.setMarca(rs.getString("marca"));
                p.setStock(rs.getInt("stock"));
                p.setAttivo(rs.getBoolean("attivo"));
                p.setSconto(rs.getInt("sconto"));
                p.setCategoria(rs.getString("categoria"));
                
                return p;
            }
        }
        return null;
    }

    public synchronized List<ProdottoBean> doRetrieveAll() throws SQLException {
        List<ProdottoBean> lista = new LinkedList<>();
        String sql = "SELECT * FROM "+TABLE_NAME;

        try (Connection con = ds.getConnection();
        		Statement st = con.createStatement()) {

            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
                ProdottoBean p = new ProdottoBean();
                p.setIdProdotto(rs.getInt("id_prodotto"));
                p.setNome(rs.getString("nome"));
                p.setModello(rs.getString("modello"));
                p.setDescrizione(rs.getString("descrizione"));
                p.setMarca(rs.getString("marca"));
                p.setStock(rs.getInt("stock"));
                p.setAttivo(rs.getBoolean("attivo"));
                p.setSconto(rs.getInt("sconto"));
                p.setCategoria(rs.getString("categoria"));
                
                lista.add(p);
            }
        }
        return lista;
    }
    
    public synchronized boolean doUpdate(ProdottoBean p) throws SQLException {

        String sql = "UPDATE " + TABLE_NAME + " SET nome=?, modello=?, descrizione=?, marca=?, prezzo=?, stock=?, attivo=?, sconto=?, categoria=? WHERE id_prodotto=?";

        try(Connection con = ds.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, p.getNome());
            ps.setString(2, p.getModello());
            ps.setString(3, p.getDescrizione());
            ps.setString(4, p.getMarca());
            ps.setDouble(5, p.getPrezzo());
            ps.setInt(6, p.getStock());
            ps.setBoolean(7, p.isAttivo());
            ps.setInt(8, p.getSconto());
            ps.setString(9, p.getCategoria());
            ps.setInt(10, p.getIdProdotto());
            return ps.executeUpdate() > 0;
        }
    }
    
    public synchronized boolean setProductStatus(int idProdotto, boolean attivo) throws SQLException {
        String sql = "UPDATE " + TABLE_NAME + " SET attivo = ? WHERE id_prodotto = ?";

        try(Connection con = ds.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setBoolean(1, attivo);
            ps.setInt(2, idProdotto);

            return ps.executeUpdate() > 0;
        }
    }
    
    @Override
    public synchronized Map<String, Integer> doCountProductsByCategory() throws SQLException {

        Map<String, Integer> result = new HashMap<>();

        String sql = "SELECT categoria, COUNT(*) AS totale FROM prodotto GROUP BY categoria";

        try (Connection con = ds.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                result.put(rs.getString("categoria"), rs.getInt("totale"));
            }
        }

        return result;
    }
}