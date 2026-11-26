
package tn.esprit.autoloc.taskservice.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "task_dependencies",
        uniqueConstraints = @UniqueConstraint(
                columnNames = {"task_id", "depends_on_task_id"}
        ))
public class TaskDependency {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id", nullable = false)
    private Task task;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "depends_on_task_id", nullable = false)
    private Task dependsOnTask;

    @Enumerated(EnumType.STRING)
    @Column(name = "dependency_type")
    private DependencyType dependencyType = DependencyType.FINISH_TO_START;

    public enum DependencyType {
        FINISH_TO_START,
        START_TO_START,
        FINISH_TO_FINISH,
        START_TO_FINISH
    }

    public TaskDependency() {}

    public Long getId() { return id; }
    public Task getTask() { return task; }
    public void setTask(Task task) { this.task = task; }
    public Task getDependsOnTask() { return dependsOnTask; }
    public void setDependsOnTask(Task dependsOnTask) { this.dependsOnTask = dependsOnTask; }
    public DependencyType getDependencyType() { return dependencyType; }
    public void setDependencyType(DependencyType dependencyType) {
        this.dependencyType = dependencyType;
    }
}
