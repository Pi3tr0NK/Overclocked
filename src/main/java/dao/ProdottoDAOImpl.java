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
    
    public synchronized List<ProdottoBean> doRetrieveNovita(int limit) throws SQLException
    {
    		List<ProdottoBean> novita = new ArrayList<>();

        String sql = "SELECT * FROM prodotto WHERE attivo = true ORDER BY id_prodotto DESC LIMIT ?";

        try (Connection con = ds.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, limit);

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {

                    ProdottoBean p = new ProdottoBean();

                    p.setIdProdotto(rs.getInt("id_prodotto"));
                    p.setNome(rs.getString("nome"));
                    p.setModello(rs.getString("modello"));
                    p.setDescrizione(rs.getString("descrizione"));
                    p.setMarca(rs.getString("marca"));
                    p.setPrezzo(rs.getDouble("prezzo"));
                    p.setStock(rs.getInt("stock"));
                    p.setAttivo(rs.getBoolean("attivo"));
                    p.setSconto(rs.getInt("sconto"));
                    p.setCategoria(rs.getString("categoria"));

                    novita.add(p);
                }
            }
        }

        return novita;
    }
    
    public synchronized List<ProdottoBean> doRetrieveBestseller(int n) throws SQLException {

        List<ProdottoBean> prodotti = new ArrayList<>();

        String sql =
            "SELECT p.*, SUM(d.quantita) AS totale_venduto " +
            "FROM prodotto p " +
            "JOIN dettagliOrdine d ON p.id_prodotto = d.fk_prodotto " +
            "JOIN ordine o ON d.fk_ordine = o.id_ordine " +
            "WHERE o.data >= CURDATE() - INTERVAL 365 DAY " +
            "AND p.attivo = true " +
            "GROUP BY p.id_prodotto " +
            "ORDER BY totale_venduto DESC " +
            "LIMIT ?";

        try (Connection con = ds.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, n);

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {

                    ProdottoBean p = new ProdottoBean();

                    p.setIdProdotto(rs.getInt("id_prodotto"));
                    p.setNome(rs.getString("nome"));
                    p.setModello(rs.getString("modello"));
                    p.setDescrizione(rs.getString("descrizione"));
                    p.setMarca(rs.getString("marca"));
                    p.setPrezzo(rs.getDouble("prezzo"));
                    p.setStock(rs.getInt("stock"));
                    p.setDimensioni(rs.getString("dimensioni"));
                    p.setPeso(rs.getString("peso"));
                    p.setAttivo(rs.getBoolean("attivo"));
                    p.setSconto(rs.getInt("sconto"));
                    p.setCategoria(rs.getString("categoria"));

                    prodotti.add(p);
                }
            }
        }

        return prodotti;
    }
    
    public synchronized List<ProdottoBean> doRetrieveCorrelati(int limit ,int idProdotto,String categoria,double prezzo) throws SQLException {

        List<ProdottoBean> correlati = new ArrayList<>();

        String sql =
            "SELECT * FROM prodotto " +
            "WHERE attivo = true " +
            "AND categoria = ? " +
            "AND prezzo BETWEEN ? AND ? " +
            "AND id_prodotto <> ? " +
            "LIMIT ?";

        try (Connection con = ds.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, categoria);
            ps.setDouble(2, prezzo - 200);
            ps.setDouble(3, prezzo + 200);
            ps.setInt(4, idProdotto);
            ps.setInt(5, limit);

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {

                    ProdottoBean p = new ProdottoBean();

                    p.setIdProdotto(rs.getInt("id_prodotto"));
                    p.setNome(rs.getString("nome"));
                    p.setModello(rs.getString("modello"));
                    p.setDescrizione(rs.getString("descrizione"));
                    p.setMarca(rs.getString("marca"));
                    p.setPrezzo(rs.getDouble("prezzo"));
                    p.setStock(rs.getInt("stock"));
                    p.setAttivo(rs.getBoolean("attivo"));
                    p.setSconto(rs.getInt("sconto"));
                    p.setCategoria(rs.getString("categoria"));

                    correlati.add(p);
                }
            }
        }

        return correlati;
    }
    
}