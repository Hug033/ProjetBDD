package bdd;

import java.io.*;
import java.util.TreeSet;

/**
 * Classe qui contient des outils de sérialization
 *
 * @author Jason Mahdjoub
 * @version 1.0
 */
class SerializationTools {
    /**
     * Serialise/binarise l'objet passé en paramètre pour retourner un tableau binaire
     *
     * @param o l'objet à serialiser
     * @return the tableau binaire
     * @throws IOException si un problème d'entrée/sortie se produit
     */
    static byte[] serialize(Serializable o) throws IOException {
        if (o != null) {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(o);
            oos.flush();
            return bos.toByteArray();
        } else
        	throw new NullPointerException();
    }

    /**
     * Désérialise le tableau binaire donné en paramètre pour retrouver l'objet initial avant sa sérialisation
     *
     * @param data le tableau binaire
     * @return l'objet désérialisé
     * @throws IOException            si un problème d'entrée/sortie se produit
     * @throws ClassNotFoundException si un problème lors de la déserialisation s'est produit
     */
    static Serializable deserialize(byte[] data) throws IOException, ClassNotFoundException {
        if (data != null) {
            ByteArrayInputStream ba = new ByteArrayInputStream(data);
            ObjectInputStream obj = new ObjectInputStream(ba);
            return (Serializable) obj.readObject();
        } else
            throw new NullPointerException();
    }

    /**
     * Serialise/binarise le tableau d'espaces libres passé en paramètre pour retourner un tableau binaire, mais selon le schéma suivant :
     * Pour chaque interval ;
     * <ul>
     *     <li>écrire en binaire la position de l'interval</li>
     *     <li>écrire en binaire la taille de l'interval</li>
     * </ul>
     * Utilisation pour cela la classe {@link DataOutputStream}
     *
     * @param freeSpaceIntervals le tableau d'espaces libres
     * @return un tableau binaire
     * @throws IOException si un problème d'entrée/sortie se produit
     */
    static byte[] serializeFreeSpaceIntervals(TreeSet<BDD.FreeSpaceInterval> freeSpaceIntervals) throws IOException {
        if (freeSpaceIntervals != null) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            DataOutputStream dos = new DataOutputStream(baos);
            byte[] result = null;
            for(BDD.FreeSpaceInterval interval : freeSpaceIntervals){
                dos.writeLong(interval.getStartPosition());
                dos.writeLong(interval.getLength());
                dos.flush();
                result = baos.toByteArray();
            }
            return result;
        } else
            throw new NullPointerException();
    }

    /**
     * Effectue l'opération inverse de la fonction {@link #serializeFreeSpaceIntervals(TreeSet)}
     *
     * @param data le tableau binaire
     * @return le tableau d'espaces libres
     * @throws IOException si un problème d'entrée/sortie se produit
     */
    static TreeSet<BDD.FreeSpaceInterval> deserializeFreeSpaceIntervals(byte[] data) throws IOException {
        if(data != null) {
            ByteArrayInputStream bis = new ByteArrayInputStream(data);
            DataInputStream dis = new DataInputStream(bis);
            Serializable o = (Serializable) dis.read(data);
            if (dis != null) {
                dis.close();
            }
            return (TreeSet<BDD.FreeSpaceInterval>) o;
        } else
            throw new NullPointerException();
    }
}
