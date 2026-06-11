package dao;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDate;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import model.CarrelloBean;
import model.CarrelloItemBean;
import model.OrdineBean;
import model.OrdineBean.Stato;
import model.ProdottoBean;
import model.UtenteBean;

public class OrdineDAOImpl implements OrdineDAO {

    private static final String TABLE_NAME = "ordine";
    private DataSource ds = null;

    public OrdineDAOImpl(DataSource ds) {
        this.ds = ds;
    }

    // ─────────────────────────────────────────────
    //  SAVE ordine semplice (usato dall'admin ecc.)
    // ─────────────────────────────────────────────

    public synchronized void doSave(OrdineBean ordine) throws SQLException {

        String sql = "INSERT INTO " + TABLE_NAME +
                     " (data, stato, totale, fattura_path, fk_utente, fk_indirizzo) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection con = ds.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setDate(1, Date.valueOf(ordine.getData()));
            ps.setString(2, ordine.getStato().name());
            ps.setDouble(3, ordine.getTotale());
            ps.setString(4, ordine.getFatturaPath());
            ps.setInt(5, ordine.getUtente().getIdUtente());
            ps.setInt(6, ordine.getIndirizzo().getIdIndirizzo());

            ps.executeUpdate();
        }
    }
    
    public synchronized OrdineBean doRetrieveByKey(int idOrdine) throws SQLException {

        String sql = "SELECT * FROM " + TABLE_NAME +
                     " WHERE id_ordine = ?";

        try (Connection con = ds.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idOrdine);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs, new IndirizzoDAOImpl(ds), new UtenteDAOImpl(ds));
                }
            }
        }
        return null;
    }

    // ─────────────────────────────────────────────
    //  SAVE ordine completo da carrello
    //  (inserisce ordine + dettagli + scala stock
    //   in un'unica transazione)
    // ─────────────────────────────────────────────

    public synchronized int doSaveOrdineCompleto(CarrelloBean cart, UtenteBean utente, int idIndirizzo)
            throws SQLException {

        double totale = 0;
        for (CarrelloItemBean item : cart.getItems()) {
            double prezzoScontato = item.getProdotto().getPrezzo()
                    * (1.0 - item.getProdotto().getSconto() / 100.0);
            totale += prezzoScontato * item.getQuantita();
        }

        Connection con = null;
        try {
            con = ds.getConnection();
            con.setAutoCommit(false);

            // 1. Inserisci ordine
            int idOrdine;
            String sqlOrdine =
                "INSERT INTO ordine (data, stato, totale, fattura_path, fk_utente, fk_indirizzo) " +
                "VALUES (?, ?, ?, NULL, ?, ?)";

            try (PreparedStatement ps = con.prepareStatement(sqlOrdine, Statement.RETURN_GENERATED_KEYS)) {
                ps.setDate(1, java.sql.Date.valueOf(LocalDate.now()));
                ps.setString(2, Stato.IN_PREPARAZIONE.name());
                ps.setDouble(3, totale);
                ps.setInt(4, utente.getIdUtente());
                ps.setInt(5, idIndirizzo);
                ps.executeUpdate();
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) idOrdine = rs.getInt(1);
                    else throw new SQLException("Id ordine non generato");
                }
            }

            // 2. Inserisci dettagli e scala stock per ogni prodotto
            String sqlDettaglio =
                "INSERT INTO dettagliOrdine (fk_ordine, fk_prodotto, quantita, prezzo_unitario) " +
                "VALUES (?, ?, ?, ?)";
            String sqlStock =
                "UPDATE prodotto SET stock = stock - ? WHERE id_prodotto = ? AND stock >= ?";

            for (CarrelloItemBean item : cart.getItems()) {
                ProdottoBean p = item.getProdotto();
                double prezzoScontato = p.getPrezzo() * (1.0 - p.getSconto() / 100.0);

                try (PreparedStatement ps = con.prepareStatement(sqlDettaglio)) {
                    ps.setInt(1, idOrdine);
                    ps.setInt(2, p.getIdProdotto());
                    ps.setInt(3, item.getQuantita());
                    ps.setDouble(4, prezzoScontato);
                    ps.executeUpdate();
                }

                try (PreparedStatement ps = con.prepareStatement(sqlStock)) {
                    ps.setInt(1, item.getQuantita());
                    ps.setInt(2, p.getIdProdotto());
                    ps.setInt(3, item.getQuantita());
                    int righe = ps.executeUpdate();
                    if (righe == 0)
                        throw new SQLException("Stock insufficiente per: " + p.getNome());
                }
            }

            con.commit();
            return idOrdine;

        } catch (SQLException e) {
            if (con != null) try { con.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            throw e;
        } finally {
            if (con != null) try { con.setAutoCommit(true); con.close(); } catch (SQLException ex) { ex.printStackTrace(); }
        }
    }

    // ─────────────────────────────────────────────
    //  RETRIEVE tutti gli ordini (admin)
    // ─────────────────────────────────────────────

    public synchronized Collection<OrdineBean> doRetrieveAll(String stato, int pagina) throws SQLException {

        List<OrdineBean> lista = new LinkedList<>();

        String sql =
            "SELECT * FROM " + TABLE_NAME +
            " WHERE (? = '' OR stato = ?)" +
            " ORDER BY data DESC" +
    	    " LIMIT ? OFFSET ?";;

        try (Connection con = ds.getConnection();
             PreparedStatement st = con.prepareStatement(sql)) {

            String filtro = (stato == null) ? "" : stato;

            st.setString(1, filtro);
            st.setString(2, filtro);
            
        	int offset = (pagina - 1) * 10;
        	st.setInt(3, 10);
        	st.setInt(4, offset);
        	
            try (ResultSet rs = st.executeQuery()) {

                IndirizzoDAOImpl indirizzoDAO = new IndirizzoDAOImpl(ds);
                UtenteDAOImpl utenteDAO = new UtenteDAOImpl(ds);

                while (rs.next()) {
                    lista.add(mapRow(rs, indirizzoDAO, utenteDAO));
                }
            }
        }

        return lista;
    }

    // ─────────────────────────────────────────────
    //  RETRIEVE ordini per utente
    // ─────────────────────────────────────────────

    public synchronized Collection<OrdineBean> doRetrieveAllByUser(int idUser) throws SQLException {

        List<OrdineBean> lista = new LinkedList<>();
        String sql = "SELECT * FROM " + TABLE_NAME +
                     " WHERE fk_utente = ? AND stato != 'RIMBORSATO' ORDER BY data DESC" +
             	    "LIMIT ? OFFSET ?";;

        try (Connection con = ds.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idUser);

            IndirizzoDAOImpl indirizzoDAO = new IndirizzoDAOImpl(ds);
            UtenteDAOImpl utenteDAO = new UtenteDAOImpl(ds);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapRow(rs, indirizzoDAO, utenteDAO));
                }
            }
        }
        return lista;
    }

    // ─────────────────────────────────────────────
    //  RETRIEVE ordini RIMBORSATI per utente (resi)
    // ─────────────────────────────────────────────

    public synchronized Collection<OrdineBean> doRetrieveResiByUser(int idUser) throws SQLException {

        List<OrdineBean> lista = new LinkedList<>();
        String sql = "SELECT * FROM " + TABLE_NAME +
                     " WHERE fk_utente = ? AND stato = 'RIMBORSATO' ORDER BY data DESC";

        try (Connection con = ds.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idUser);

            IndirizzoDAOImpl indirizzoDAO = new IndirizzoDAOImpl(ds);
            UtenteDAOImpl utenteDAO = new UtenteDAOImpl(ds);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapRow(rs, indirizzoDAO, utenteDAO));
                }
            }
        }
        return lista;
    }

    // ─────────────────────────────────────────────
    //  RIMBORSA ordine
    //  Verifica 30 giorni, ripristina stock,
    //  imposta stato RIMBORSATO — tutto in una
    //  unica transazione
    // ─────────────────────────────────────────────

    public synchronized boolean doRimborsa(int idOrdine, int idUtente) throws SQLException {

        Connection con = null;
        try {
            con = ds.getConnection();
            con.setAutoCommit(false);

            // 1. Verifica che l'ordine esista, appartenga all'utente
            //    e non sia già rimborsato
            String sqlCheck =
                "SELECT id_ordine, data FROM " + TABLE_NAME +
                " WHERE id_ordine = ? AND fk_utente = ? AND stato != 'RIMBORSATO'";

            LocalDate dataOrdine;
            try (PreparedStatement ps = con.prepareStatement(sqlCheck)) {
                ps.setInt(1, idOrdine);
                ps.setInt(2, idUtente);
                try (ResultSet rs = ps.executeQuery()) {
                    if (!rs.next()) return false;
                    dataOrdine = rs.getDate("data").toLocalDate();
                }
            }

            // 2. Verifica che siano passati meno di 30 giorni
            if (dataOrdine.isBefore(LocalDate.now().minusDays(30))) {
                return false;
            }

            // 3. Ripristina lo stock per ogni prodotto dell'ordine
            String sqlDettagli =
                "SELECT fk_prodotto, quantita FROM dettagliOrdine WHERE fk_ordine = ?";
            String sqlStock =
                "UPDATE prodotto SET stock = stock + ? WHERE id_prodotto = ?";

            try (PreparedStatement psD = con.prepareStatement(sqlDettagli)) {
                psD.setInt(1, idOrdine);
                try (ResultSet rs = psD.executeQuery()) {
                    while (rs.next()) {
                        try (PreparedStatement psS = con.prepareStatement(sqlStock)) {
                            psS.setInt(1, rs.getInt("quantita"));
                            psS.setInt(2, rs.getInt("fk_prodotto"));
                            psS.executeUpdate();
                        }
                    }
                }
            }

            // 4. Imposta stato RIMBORSATO
            String sqlUpdate =
                "UPDATE " + TABLE_NAME + " SET stato = 'RIMBORSATO' WHERE id_ordine = ?";
            try (PreparedStatement ps = con.prepareStatement(sqlUpdate)) {
                ps.setInt(1, idOrdine);
                ps.executeUpdate();
            }

            con.commit();
            return true;

        } catch (SQLException e) {
            if (con != null) try { con.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            throw e;
        } finally {
            if (con != null) try { con.setAutoCommit(true); con.close(); } catch (SQLException ex) { ex.printStackTrace(); }
        }
    }

    // ─────────────────────────────────────────────
    //  UPDATE ordine
    // ─────────────────────────────────────────────

    public synchronized boolean doUpdate(OrdineBean o) throws SQLException {

        String sql = "UPDATE " + TABLE_NAME +
                     " SET data=?, stato=?, totale=?, fattura_path=?, fk_utente=?, fk_indirizzo=?" +
                     " WHERE id_ordine=?";

        try (Connection con = ds.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setDate(1, Date.valueOf(o.getData()));
            ps.setString(2, o.getStato().name());
            ps.setDouble(3, o.getTotale());
            ps.setString(4, o.getFatturaPath());
            ps.setInt(5, o.getUtente().getIdUtente());
            ps.setInt(6, o.getIndirizzo().getIdIndirizzo());
            ps.setInt(7, o.getIdOrdine());

            return ps.executeUpdate() > 0;
        }
    }

    // ─────────────────────────────────────────────
    //  SET stato ordine
    // ─────────────────────────────────────────────

    public synchronized boolean setOrdineStatus(int idOrdine, Stato stato) throws SQLException {

        String sql = "UPDATE " + TABLE_NAME + " SET stato=? WHERE id_ordine=?";

        try (Connection con = ds.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, stato.name());
            ps.setInt(2, idOrdine);

            return ps.executeUpdate() > 0;
        }
    }

    // ─────────────────────────────────────────────
    //  COUNT ordini ultimo mese
    // ─────────────────────────────────────────────

    public synchronized int doCountByLastMonth() throws SQLException {

        String sql = "SELECT COUNT(*) FROM " + TABLE_NAME +
                     " WHERE data >= DATE_SUB(NOW(), INTERVAL 1 MONTH)";

        try (Connection con = ds.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) return rs.getInt(1);
        }
        return 0;
    }
    
    // ─────────────────────────────────────────────
    //  COUNT ordini per stato
    // ─────────────────────────────────────────────

    public synchronized int doCount(String stato) throws SQLException {

        boolean filtra = stato != null && !stato.isBlank();

        String sql = "SELECT COUNT(*) FROM " + TABLE_NAME
                   + (filtra ? " WHERE stato = ?" : "");

        try (Connection con = ds.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            if (filtra) {
                ps.setString(1, stato);
            }

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1);
            }
        }

        return 0;
    }

    // ─────────────────────────────────────────────
    //  Utility: mappa una riga ResultSet in OrdineBean
    // ─────────────────────────────────────────────

    private OrdineBean mapRow(ResultSet rs, IndirizzoDAOImpl indirizzoDAO, UtenteDAOImpl utenteDAO)
            throws SQLException {

        OrdineBean o = new OrdineBean();
        o.setIdOrdine(rs.getInt("id_ordine"));
        o.setData(rs.getDate("data").toLocalDate());
        o.setStato(Stato.valueOf(rs.getString("stato")));
        o.setTotale(rs.getDouble("totale"));
        o.setFatturaPath(rs.getString("fattura_path"));
        o.setIndirizzo(indirizzoDAO.doRetrieveByKey(rs.getInt("fk_indirizzo")));
        o.setUtente(utenteDAO.doRetrieveByKey(rs.getInt("fk_utente")));
        
        
        return o;
        
        
    }
}