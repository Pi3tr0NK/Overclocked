package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import javax.sql.DataSource;

import model.IndirizzoBean;

public class IndirizzoDAOImpl implements IndirizzoDAO {
	
	private static final String TABLE_NAME = "immagini";
    private DataSource ds = null;

    public IndirizzoDAOImpl(DataSource ds) {
        this.ds = ds;
    }

    @Override
    public synchronized void doSave(IndirizzoBean indirizzo) throws SQLException {
        String sql = "INSERT INTO "+TABLE_NAME+" (via_numciv, paese, citta, provincia, dati_plus, codice_postale) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection con = ds.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, indirizzo.getViaNumciv());
            ps.setString(2, indirizzo.getPaese());
            ps.setString(3, indirizzo.getCitta());
            ps.setString(4, indirizzo.getProvincia());
            ps.setString(5, indirizzo.getDatiPlus());
            ps.setString(6, indirizzo.getCodicePostale());

            ps.executeUpdate();
        }
    }

    @Override
    public synchronized IndirizzoBean doRetrieveByKey(int id) throws SQLException {
        String sql = "SELECT * FROM "+TABLE_NAME+" WHERE id_indirizzo = ?";

        try (Connection con = ds.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                IndirizzoBean i = new IndirizzoBean();
                
                i.setViaNumciv(rs.getString("via_numciv"));
                i.setPaese(rs.getString("paese"));
                i.setCitta(rs.getString("citta"));
                i.setProvincia(rs.getString("provincia"));
                i.setDatiPlus(rs.getString("dati_plus"));
                i.setCodicePostale(rs.getString("codice_postale"));
                return i;
            }
        }
        return null;
    }

    @Override
    public synchronized List<IndirizzoBean> doRetrieveAll() throws SQLException {
        String sql = "SELECT * FROM "+TABLE_NAME ;
        List<IndirizzoBean> lista = new LinkedList<>();

        try (Connection con = ds.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
        		
             ResultSet rs = ps.executeQuery()) {
            
        	
            while (rs.next()) {
                IndirizzoBean i = new IndirizzoBean();
                
                i.setViaNumciv(rs.getString("via_numciv"));
                i.setPaese(rs.getString("paese"));
                i.setCitta(rs.getString("citta"));
                i.setProvincia(rs.getString("provincia"));
                i.setDatiPlus(rs.getString("dati_plus"));
                i.setCodicePostale(rs.getString("codice_postale"));
                lista.add(i);
            }
        }

        return lista;
    }

    @Override
    public synchronized boolean doUpdate(IndirizzoBean indirizzo) throws SQLException {
        String sql = "UPDATE "+TABLE_NAME+" SET via_numciv=?, paese=?, citta=?, provincia=?, dati_plus=?, codice_postale=? WHERE id_indirizzo=?";

        try (Connection con = ds.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, indirizzo.getViaNumciv());
            ps.setString(2, indirizzo.getPaese());
            ps.setString(3, indirizzo.getCitta());
            ps.setString(4, indirizzo.getProvincia());
            ps.setString(5, indirizzo.getDatiPlus());
            ps.setString(6, indirizzo.getCodicePostale());
            ps.setInt(7, indirizzo.getIdIndirizzo());

            return ps.executeUpdate() > 0;
        }
    }
}
