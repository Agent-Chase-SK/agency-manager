/**
 *
 * @author Jakub Suslik
 */
package cz.muni.fi.pv168.agencymanager.manager;

/**
 *
 * @author Jakub Suslik
 * @param <T>
 */
public interface Operation<T> {
    void callOn(T subjectOfOperation);
}
