package CancelGPT.core;

import CancelGPT.exception.task.InvalidTask;
import CancelGPT.task.Task;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class TasksStorage {
    private CancelGPT chatbot;
    private Path tasksStoragePath;

    public TasksStorage(CancelGPT chatbot, Path tasksStorageDirectoryPath) throws IOException {
        this.chatbot = chatbot;

        if (!Files.exists(tasksStorageDirectoryPath)) {
            Files.createDirectories(tasksStorageDirectoryPath);
        }
        this.tasksStoragePath = Paths.get(tasksStorageDirectoryPath.toString(), chatbot.getName() + ".txt");
        if (!Files.exists(this.tasksStoragePath)) {
            Files.createFile(this.tasksStoragePath);
        }
        this.readTaskStorageToTasksList();
    }

    private void readTaskStorageToTasksList() throws IOException {
        Scanner tasksStorageReader = new Scanner(this.tasksStoragePath);
        while (tasksStorageReader.hasNextLine()) {
            try {
                this.chatbot.addToTaskList(Task.getTaskFromSavedDataString(tasksStorageReader.nextLine()));
            } catch (InvalidTask e) {
                System.out.println(e.getMessage());
            }
        }
        tasksStorageReader.close();
    }

    public void saveTasks() throws IOException {
        FileWriter tasksStorageSaver = new FileWriter(this.tasksStoragePath.toString());
        for (Task task : this.chatbot.getTasks()) {
            tasksStorageSaver.write(task.getSavedDataString());
            tasksStorageSaver.write("\n");
        }
        tasksStorageSaver.close();
    }
}
