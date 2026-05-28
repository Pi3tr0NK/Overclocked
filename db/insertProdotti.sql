-- PSU
INSERT INTO prodotto(nome, modello, descrizione, marca, prezzo, stock, dimensioni, peso, attivo, sconto)
VALUES
('Alimentatore 750W', 'RM750e', 'Alimentatore modulare 80 Plus Gold', 'Corsair', 129.99, 15, '150x86x160mm', '1.8kg', true, 10);

INSERT INTO psu(fk_prodotto, potenza, certificazione, modulare, formato)
VALUES
(LAST_INSERT_ID(), 750, '80 Plus Gold', 'MODULARE', 'ATX');


-- Dissipatore
INSERT INTO prodotto(nome, modello, descrizione, marca, prezzo, stock, dimensioni, peso, attivo, sconto)
VALUES
('Dissipatore ad aria', 'AK400', 'Dissipatore tower silenzioso', 'DeepCool', 39.99, 20, '155mm', '0.7kg', true, 0);

INSERT INTO dissipatore(fk_prodotto, tipo, socket_supportati, dimensioni_ventola, rpm_max, rumore, tdp_supportato)
VALUES
(LAST_INSERT_ID(), 'ARIA', 'AM4,AM5,LGA1700', '120mm', 1850, 29, 220);


-- CPU
INSERT INTO prodotto(nome, modello, descrizione, marca, prezzo, stock, dimensioni, peso, attivo, sconto)
VALUES
('Processore Ryzen 7', '7800X3D', 'CPU gaming ad alte prestazioni', 'AMD', 399.99, 8, NULL, NULL, true, 5);

INSERT INTO cpu(fk_prodotto, core, thread, frequenza, frequenza_ram, tiporam, socket, tdp)
VALUES
(LAST_INSERT_ID(), 8, 16, '4.2GHz', 'DDR5-5200', 'DDR5', 'AM5', 120);


-- RAM
INSERT INTO prodotto(nome, modello, descrizione, marca, prezzo, stock, dimensioni, peso, attivo, sconto)
VALUES
('RAM 32GB DDR5', 'Vengeance', 'Kit RAM DDR5 ad alta velocità', 'Corsair', 149.99, 30, NULL, NULL, true, 15);

INSERT INTO ram(fk_prodotto, capacita, frequenza, tipo)
VALUES
(LAST_INSERT_ID(), '32GB', '6000MHz', 'DDR5');


-- Chassis
INSERT INTO prodotto(nome, modello, descrizione, marca, prezzo, stock, dimensioni, peso, attivo, sconto)
VALUES
('Case Mid Tower', 'H5 Flow', 'Case con airflow ottimizzato', 'NZXT', 109.99, 10, '465x227x446mm', '7.3kg', true, 0);

INSERT INTO chassis(fk_prodotto, formato, colore, materiale)
VALUES
(LAST_INSERT_ID(), 'ATX', 'Nero', 'Acciaio e vetro temperato');


-- GPU
INSERT INTO prodotto(nome, modello, descrizione, marca, prezzo, stock, dimensioni, peso, attivo, sconto)
VALUES
('Scheda Video RTX 4070', 'RTX 4070 Dual', 'GPU NVIDIA di fascia alta', 'ASUS', 649.99, 5, '267mm', '1.1kg', true, 5);

INSERT INTO gpu(fk_prodotto, frequenza, vram, video, tipovram, pcie, maxres, tdp)
VALUES
(LAST_INSERT_ID(), '2475MHz', '12GB', 'HDMI, DisplayPort', 'GDDR6X', 'PCIe 4.0', '7680x4320', 200);


-- Scheda Madre
INSERT INTO prodotto(nome, modello, descrizione, marca, prezzo, stock, dimensioni, peso, attivo, sconto)
VALUES
('Scheda Madre B650', 'B650 Gaming X AX', 'Scheda madre AM5 ATX', 'Gigabyte', 219.99, 12, 'ATX', NULL, true, 0);

INSERT INTO mobo(fk_prodotto, chipset, socket, tiporam, maxfreq, formato, pcie, slotram, nvme, portesata, porteusb)
VALUES
(LAST_INSERT_ID(), 'B650', 'AM5', 'DDR5', '6400MHz', 'ATX', 'PCIe 4.0', 4, true, 4, 8);


-- Memoria SSD
INSERT INTO prodotto(nome, modello, descrizione, marca, prezzo, stock, dimensioni, peso, attivo, sconto)
VALUES
('SSD NVMe 1TB', '980 Pro', 'SSD ad alte prestazioni PCIe 4.0', 'Samsung', 119.99, 25, '2280', NULL, true, 10);

INSERT INTO memoria(fk_prodotto, capacita, vel_scrittura, vel_lettura, tipo, tecnologia, formato)
VALUES
(LAST_INSERT_ID(), '1TB', 5000, 7000, 'SSD', 'NVMe', 'M.2');


-- Seconda CPU
INSERT INTO prodotto(nome, modello, descrizione, marca, prezzo, stock, dimensioni, peso, attivo, sconto)
VALUES
('Processore Intel Core i5', '14600K', 'CPU Intel di nuova generazione', 'Intel', 329.99, 7, NULL, NULL, true, 0);

INSERT INTO cpu(fk_prodotto, core, thread, frequenza, frequenza_ram, tiporam, socket, tdp)
VALUES
(LAST_INSERT_ID(), 14, 20, '5.3GHz', 'DDR5-5600', 'DDR5', 'LGA1700', 125);


-- Seconda GPU
INSERT INTO prodotto(nome, modello, descrizione, marca, prezzo, stock, dimensioni, peso, attivo, sconto)
VALUES
('Scheda Video RX 7800 XT', 'Pulse RX 7800 XT', 'GPU AMD per gaming 2K/4K', 'Sapphire', 579.99, 6, '320mm', '1.4kg', true, 8);

INSERT INTO gpu(fk_prodotto, frequenza, vram, video, tipovram, pcie, maxres, tdp)
VALUES
(LAST_INSERT_ID(), '2430MHz', '16GB', 'HDMI, DisplayPort', 'GDDR6', 'PCIe 4.0', '7680x4320', 263);