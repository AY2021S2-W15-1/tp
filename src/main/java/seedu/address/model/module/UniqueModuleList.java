package seedu.address.model.module;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.model.module.exceptions.DuplicateModuleException;
import seedu.address.model.module.exceptions.ModuleNotFoundException;


public class UniqueModuleList implements Iterable<Module> {

    private final ObservableList<Module> internalList = FXCollections.observableArrayList();
    private final ObservableList<Module> internalUnmodifiableList =
            FXCollections.unmodifiableObservableList(internalList);

    /**
     * Returns true if the list contains an equivalent module as the given argument.
     */
    public boolean contains(Module toCheck) {
        requireNonNull(toCheck);
        return internalList.stream().anyMatch(toCheck::isSameModule);
    }

    /**
     * Adds a module to the list.
     * The module must not already exit in the list.
     */
    public void add(Module toAdd) {
        requireNonNull(toAdd);
        if (contains(toAdd)) {
            throw new DuplicateModuleException();
        }
        internalList.add(toAdd);
    }

    /**
     * Replaces the module {@code target} in the list with {@code editedPerson}.
     * {@code target} must exist in the list.
     * The module identity of {@code editedModule} must not be the same as another existing
     * module in the list.
     */
    //todo I think this is for editCommand. Delete if not used.
    public void setModule(Module target, Module editedModule) {
        requireAllNonNull(target, editedModule);

        int index = internalList.indexOf(target);
        if (index == -1) {
            throw new ModuleNotFoundException();
        }

        boolean hasDuplicate = !target.isSameModule(editedModule) && contains(editedModule);
        if (hasDuplicate) {
            throw new DuplicateModuleException();
        }

        internalList.set(index, editedModule);
    }

    /**
     * Removes the equivalent module from the list.
     * The module must exist in the list.
     */
    public void remove(Module toRemove) {
        requireNonNull(toRemove);
        if (!internalList.remove(toRemove)) {
            throw new ModuleNotFoundException();
        }
    }

    public void setModules(UniqueModuleList replaceModules) {
        requireNonNull(replaceModules);
        internalList.setAll(replaceModules.internalList);
    }

    /**
     * Replaces the contents of this list with {@code modules}.
     * {@code modules} must not contain duplicate modules.
     */
    public void setModules(List<Module> modules) {
        requireAllNonNull(modules);
        if (!areModulesUnique(modules)) {
            throw new DuplicateModuleException();
        }

        internalList.setAll(modules);
    }

    /**
     * Returns the backing list as an unmodifiable {@code ObservableList}.
     */
    // todo not sure if we need this
    public ObservableList<Module> asUnmodifiableObservableList() {
        return internalUnmodifiableList;
    }

    @Override
    public Iterator<Module> iterator() {
        return internalList.iterator();
    }

    @Override
    public boolean equals(Object other) {
        return other == this
                || (other instanceof UniqueModuleList)
                    && internalList.equals(((UniqueModuleList) other).internalList);
    }

    @Override
    public int hashCode() {
        return internalList.hashCode();
    }

    /**
     * Returns true if {@code modules} contains only unique modules.
     */
    private boolean areModulesUnique(List<Module> modules) {
        // todo trying something different, hope it works.
        sort(modules);
        for (int i = 0; i < modules.size(); i++) {
            Module currModule = modules.get(i);
            Module nextModule = modules.get(i + 1);
            if (currModule.isSameModule(nextModule)) {
                return false;
            }
        }
        return true;
    }

    private void sort(List<Module> modules) {
        Collections.sort(modules);
    }
}