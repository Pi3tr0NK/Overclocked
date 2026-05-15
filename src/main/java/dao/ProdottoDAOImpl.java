package dao;

import java.sql.*;
import java.util.*;

import javax.sql.DataSource;

import model.ProdottoBean;

public class ProdottoDAOImpl {
	private static final String TABLE_NAME = "prodotto";
    private DataSource ds = null;
    
    public ProdottoDAOImpl(DataSource ds) {
        this.ds = ds;
    }
	
    public void doSave(ProdottoBean p) throws SQLException {
        String sql = "INSERT INTO prodotto (nome, modello, descrizione, marca, prezzo, stock, attivo) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = ds.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, p.getNome());
            ps.setString(2, p.getModello());
            ps.setString(3, p.getDescrizione());
            ps.setString(4, p.getMarca());
            ps.setDouble(5, p.getPrezzo());
            ps.setInt(6, p.getStock());
            ps.setBoolean(7, p.isAttivo());

            ps.executeUpdate();
        }
    }

    public ProdottoBean doRetrieveByKey(int id) throws SQLException {
        String sql = "SELECT * FROM prodotto WHERE id_prodotto = ?";

        try (Connection con = ds.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                ProdottoBean p = new ProdottoBean();
                p.setIdProdotto(rs.getInt("id_prodotto"));
                p.setNome(rs.getString("nome"));
                p.setPrezzo(rs.getDouble("prezzo"));
                return p;
            }
        }
        return null;
    }

    public List<ProdottoBean> doRetrieveAll() throws SQLException {
        List<ProdottoBean> lista = new ArrayList<>();
        String sql = "SELECT * FROM prodotto";

        try (Connection con = ds.getConnection();
             Statement st = con.createStatement()) {

            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
                ProdottoBean p = new ProdottoBean();
                p.setIdProdotto(rs.getInt("id_prodotto"));
                p.setNome(rs.getString("nome"));
                p.setPrezzo(rs.getDouble("prezzo"));
                lista.add(p);
            }
        }
        return lista;
    }

    public boolean doDelete(int id) throws SQLException {
        String sql = "DELETE FROM prodotto WHERE id_prodotto = ?";

        try (Connection con = ds.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }
}