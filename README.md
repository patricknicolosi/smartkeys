<h1>Progetto del corso Programmazione Mobile</h1>
<h2>Specifiche</h2>
Automazione/ apertura varco (configurabile per più posizioni)
Un varco è caratterizzato dalla posizione GPS e da un codice (QR e/o NFC tag)
L’app riconosce un varco se è stata fatta la configurazione. 

Configurazione 
Registrazione/Autenticazione  
L’aggiunta di un varco prevede la registrazione che deve essere  confermata dalla lettura di un qr-code ( a disposizione del “gestore”) che tra l’altro permette di ottenere l’url per l’apertura del varco e un -  TOKEN univoco (per varco/utente).

Quando un utente si avvicina ad un varco legge il qr-code/NFC tag  farà la richiesta di apertura invocando il servizio attraverso l’url ottenuto in fase di registrazione passando come parametri il token anch’esso ottenuto  in fase di registrazione.

FCM - per la comunicazione di autorizzazione e/o diniego accesso.

Logging accessi autorizzati e non autorizzati

<h2>Relazione</h2>
<a href="https://github.com/patricknicolosi/smartkeys/files/11452753/Relazione.pdf" target="_blank">Link alla relazione</a>
