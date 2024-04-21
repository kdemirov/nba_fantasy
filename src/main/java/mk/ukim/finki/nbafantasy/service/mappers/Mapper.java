package mk.ukim.finki.nbafantasy.service.mappers;

/**
 * Mapper interface from entity object to data transfer object.
 *
 * @param <F> entity object
 * @param <T> data transfer object
 */
public interface Mapper<F, T> {

    /**
     * Maps given entity model to presentational model.
     *
     * @param entity
     * @return F presentation model
     */
    T map(F entity);
}
