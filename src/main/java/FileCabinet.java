import javax.management.ObjectInstance;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

interface Cabinet {
    // zwraca dowolny element o podanej nazwie
    Optional<Folder>
    findFolderByName(String name);

    // zwraca wszystkie foldery podanego rozmiaru SMALL/MEDIUM/LARGE
    List<Folder> findFoldersBySize(String size);

    //zwraca liczbę wszystkich obiektów tworzących strukturę
    int count();
}

interface Folder {
    String getName();
    String getSize();
}

interface MultiFolder extends Folder {
    List<Folder> getFolders();
}

public class FileCabinet implements Cabinet {
    private List<Folder> folders;

    public Optional<Folder> findFolderByName(String name) {
        for (Folder object: folders) {
            if (object.getClass().getCanonicalName().equals("MultiFolder")) {
                MultiFolder tmpMultiFolder = (MultiFolder) object;
                for (Folder multiObject: tmpMultiFolder.getFolders()) {
                    if (multiObject.getName().equals(name)) {
                        return Optional.of(multiObject);
                    }
                }
            } else {
                if (object.getName().equals(name)) {
                    return Optional.of(object);
                }
            }
        }

        return Optional.empty();
    }

    public List<Folder> findFoldersBySize(String size) {
        List<Folder> answer = new ArrayList<>();

        for (Folder object: folders) {
            if (object.getClass().getCanonicalName().equals("MultiFolder")) {
                MultiFolder tmpMultiFolder = (MultiFolder) object;
                for (Folder multiObject: tmpMultiFolder.getFolders()) {
                    if (multiObject.getSize().equals(size)) {
                        answer.add(multiObject);
                    }
                }
            } else {
                if (object.getSize().equals(size)) {
                    answer.add(object);
                }
            }
        }

        return answer;
    }

    public int count() {
        int counter = 0;

        if (folders.isEmpty()) {
            return counter;
        }

        for (Folder object: folders) {
            if (object.getClass().getCanonicalName().equals("MultiFolder")) {
                MultiFolder tmpMultiFolder = (MultiFolder) object;
                counter += tmpMultiFolder.getFolders().size();
            } else {
                counter++;
            }
        }

        return counter;
    }
}
