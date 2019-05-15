package org.devgateway.toolkit.persistence.dao.form;

/**
 * @author mihai
 * <p>
 * Interface assigned to any entity that can/will be attached to a {@link Project} entity
 */
public interface ProjectAttachable {

    /**
     * Default behavior to get a {@link Project}. It will return the project attached to the attachable
     * This should be overriden if the {@link Project} is readily available, for eg. in
     * {@link PurchaseRequisition}
     *
     * @return the attached project entity
     */
    default Project getProject() {
        return getProject(getProjectAttachable());
    }

    /**
     * @return The delegated {@link ProjectAttachable} that will be responsible for returing the {@link Project}
     */
    ProjectAttachable getProjectAttachable();

    /**
     * @return Get the id of the project, if {@link Project} is present
     */
    default Long getProjectId() {
        Project project = getProject();
        if (project != null) {
            return getProject().getId();
        }
        return null;
    }

    /**
     * @return true if it has a {@link Project} with {@link Project#getId()} not null
     */
    default boolean hasProjectId() {
        return getProjectId() != null;
    }

    /**
     * Helper method to get the {@link Project} from a non-null {@link ProjectAttachable}
     *
     * @param projectAttachable the {@link ProjectAttachable} to get the {@link Project} from
     * @return the Project, if any
     */
    static Project getProject(ProjectAttachable projectAttachable) {
        if (projectAttachable != null) {
            return projectAttachable.getProject();
        }
        return null;
    }

}
