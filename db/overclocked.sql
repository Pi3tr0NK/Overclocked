CREATE DATABASE overclocked;
USE overclocked;

CREATE TABLE indirizzo(
	id_indirizzo int PRIMARY KEY AUTO_INCREMENT,
    via_numciv varchar(255),
    paese varchar(100),
    num_civ int,
    citta varchar(100),
    provincia varchar(100),
	dati_plus varchar(255),
    codice_postale varchar(10)
);

CREATE TABLE utente(
	id_utente int PRIMARY KEY AUTO_INCREMENT,
    email varchar(255) NOT NULL UNIQUE,
    nome varchar(20) NOT NULL,
    cognome varchar(20) NOT NULL,
    password varchar(255) NOT NULL,
    ruolo enum('user','admin') NOT NULL DEFAULT('user'),
    cellulare varchar(50),
    
    FOREIGN KEY(PatientId) REFERENCES Patient(id) ON UPDATE cascade ON DELETE cascade
);