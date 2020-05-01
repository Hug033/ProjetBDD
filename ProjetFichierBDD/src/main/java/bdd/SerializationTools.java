package bdd;

import java.io.*;
import java.util.Arrays;
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
            dos.flush();
            for(BDD.FreeSpaceInterval interval : freeSpaceIntervals){
                dos.writeLong(interval.getStartPosition());
                dos.writeLong(interval.getLength());
                dos.flush();
            }
            dos.close();
            baos.close();
            return baos.toByteArray();
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
            TreeSet<BDD.FreeSpaceInterval> freeSpaceInterval = new TreeSet<BDD.FreeSpaceInterval>();
            for(int i = 0; i < data.length; i += 16){
                ByteArrayInputStream tab = new ByteArrayInputStream(Arrays.copyOfRange(data, i, i+8));
                DataInputStream obj = new DataInputStream(tab);
                ByteArrayInputStream tab_2 = new ByteArrayInputStream(Arrays.copyOfRange(data, i+8, i+16));
                DataInputStream obj_2 = new DataInputStream(tab);
                freeSpaceInterval.add(new BDD.FreeSpaceInterval((long)tab.read(), (long)tab_2.read()));
                tab.close();
                obj.close();
            }
            return freeSpaceInterval;
        } else
            throw new NullPointerException();
    }
}
