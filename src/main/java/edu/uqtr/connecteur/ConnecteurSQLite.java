package edu.uqtr.connecteur;

import java.sql.*;

/**
 * Permet de manipuler simplement les bases de données SQLite3. En SQLite, les requêtes sont similaires à celles
 * des SGBD relationnels tels MySQL, PostgreSQL, SqlServer ou Oracle.
 *
 * Attention, les requêtes CREATE DATABASE et DROP DATABASE n'existent pas en SQLite.
 *
 * Pour utiliser cette classe dans vos projets, il suffit d'importer ce fichier et de modifier le nom de la bd
 * à utiliser (première constante de la classe).
 */
public class ConnecteurSQLite {

    /**
     * Indiquez ici le nom de la base de données à manipuler.
     */
    private static final String NOM_BD = "test.sqlite";

    /**
     * Implémentation de singleton
     */
    private static ConnecteurSQLite instance;

    /**
     * Chaîne de caractère servant à la connexion
     */
    private final String urlConnexion;

    /**
     * Crée un nouveau connecteur SQLite.
     *
     * Ne doit pas être appelé hors de la classe (utilisez {@link #getInstance()} pour
     * obtenir une instance.)
     */
    private ConnecteurSQLite() {
        urlConnexion = "jdbc:sqlite:" + NOM_BD;
    }

    /**
     * Crée ou retourne une instance du connecteur SQLite. La même instance est persistante
     * tout au long de l'exécution.
     *
     * @return Une instance du connecteur SQlite pour la base de données.
     */
    public static ConnecteurSQLite getInstance() {
        if (instance == null)
            instance = new ConnecteurSQLite();

        return instance;
    }

    /**
     * Exécute une requête SQL qui ne retourne pas de résultat.
     *
     * Requêtes supportées (non exhaustif) : CREATE TABLE, DROP TABLE, INSERT INTO, UPDATE, DELETE.
     * Requêtes non supportées (non exhaustif) : SELECT, SELECT INTO
     *
     * Si une exception est levée, la requête n'est pas exécutée et le message de l'exception s'affiche
     * dans sortie standard d'erreur {@code System.err }
     *
     * @param requeteSql le code SQL a exécuter. Attention, la requête doit se terminer par un ;
     * @return <i>true</i> si la requête s'exécute correctement; <i>false</i> si une erreur se produit.
     */
    public boolean executerRequete(String requeteSql) {
        Connection connexion = ouvrirConnexion();

        try {
            Statement requete = connexion.createStatement();
            requete.execute(requeteSql);

            return true;
        } catch (SQLException exception) {
            System.err.println(exception.getMessage());
        } finally {
            fermerConnexion(connexion);
        }

        return false;
    }

    /**
     * Exécute une requête SQL qui retourne un résultat, telle une sélection
     *
     * Requêtes supportées (non exhaustif) : SELECT, SELECT INTO
     * Requêtes non supportées (non exhaustif) : CREATE TABLE, DROP TABLE, INSERT INTO, UPDATE, DELETE.
     *
     * Si une exception est levée, la requête n'est pas exécutée et le message de l'exception s'affiche
     * dans sortie standard d'erreur {@code System.err }
     *
     * @param requeteSql le code SQL a exécuter. Attention, la requête doit se terminer par un ;
     * @return Un {@link ResultSet} contenant le résultat de la sélection, ou <i>null</i> si une erreur se produit
     * pendant la lecture.
     */
    public ResultSet selectionner(String requeteSql) {
        Connection connexion = ouvrirConnexion();

        try {
            Statement requete = connexion.createStatement();
            ResultSet resultat = requete.executeQuery(requeteSql);

            return resultat;
        } catch (SQLException exception) {
            System.err.println(exception.getMessage());
        } finally {
            fermerConnexion(connexion);
        }

        return null;
    }

    /**
     * Ouvre la connexion avec la base de données.
     *
     * Si une exception est levée, la connexion n'est pas établie et le message de l'exception s'affiche
     * dans sortie standard d'erreur {@code System.err }
     *
     * @return Un objet de connexion ou <i>null</i> si une erreur survient pendant l'ouverture.
     */
    private Connection ouvrirConnexion() {

        try {
            return DriverManager.getConnection(urlConnexion);
        } catch (SQLException exception) {
            System.err.println(exception.getMessage());
        }

        return null;
    }

    /**
     * Ferme la connexion avec la base de données.
     *
     * Si une exception est levée, la connexion n'est pas fermée et le message de l'exception s'affiche
     * dans sortie standard d'erreur {@code System.err }
     *
     * @return <i>true</i> si la connexion est bien fermée, <i>false</i> si une erreur est survenue.
     *
     */
    private boolean fermerConnexion(Connection connexion) {
        try {
            connexion.close();

            return true;
        } catch (SQLException exception) {
            System.err.println(exception.getMessage());
        }

        return false;
    }
}
