package persestince;

import java.util.List;

public interface CommonDAO<E, I> {

    List<E> getAll();

    E getOneByID(I id);

    boolean addNewEntity(E entity);

    boolean updateEntityInfo(E entity);

}
