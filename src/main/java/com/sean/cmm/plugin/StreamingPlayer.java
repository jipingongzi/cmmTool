package com.sean.cmm.plugin;

import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;
import javafx.util.Duration;

public class StreamingPlayer extends Application {

    private static final String MEDIA_URL = "https://video3.vod.tv.itc.cn/ETUldzUBljmCuoLrCTmviTUOxkwqT9fOskv5HO3DSxi50tmJUxkoytHrChRYAtixIWY.mp4";
    private MediaPlayer mediaPlayer;
    private ProgressBar progressBar;
    private Label progressLabel;
    private Label timeLabel;
    private DoubleProperty progressProperty = new SimpleDoubleProperty(0);
    private Tooltip progressTooltip;

    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage primaryStage) {
        // Set up media components.
        Media media = new Media(MEDIA_URL);
        mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setAutoPlay(true);
        mediaPlayer.setOnEndOfMedia(() -> mediaPlayer.seek(Duration.ZERO));
        mediaPlayer.currentTimeProperty().addListener((observable, oldValue, newValue) -> updateProgress());
        mediaPlayer.bufferProgressTimeProperty().addListener((observable, oldValue, newValue) -> updateProgress());

        // Set up progress bar.
        progressBar = new ProgressBar();
        progressBar.setMaxWidth(Double.MAX_VALUE);
        progressBar.progressProperty().bind(progressProperty);

        // Set up progress tooltip.
        progressTooltip = new Tooltip();
        Tooltip.install(progressBar, progressTooltip);

        // Allow seeking by clicking on the progress bar.
        progressBar.setOnMouseClicked(event -> {
            double mouseX = event.getX();
            double width = progressBar.getWidth();
            double newPlaybackPosition = (mouseX / width);
            mediaPlayer.seek(Duration.seconds(media.getDuration().toSeconds() * newPlaybackPosition));
        });

        // Set up progress label.
        progressLabel = new Label();
        progressLabel.setText("0%");
        progressLabel.textProperty().bind(Bindings.format("%.0f%%", progressBar.progressProperty().multiply(100)));

        // Set up time label.
        timeLabel = new Label();
        timeLabel.setText("");

        Button fastForward = new Button(">>");
        fastForward.setOnAction(event -> mediaPlayer.seek(mediaPlayer.getCurrentTime().add(Duration.seconds(5))));

        Button fastRewind = new Button("<<");
        fastRewind.setOnAction(event -> mediaPlayer.seek(mediaPlayer.getCurrentTime().subtract(Duration.seconds(5))));

        Button pause = new Button("| |");
        pause.setOnAction(event -> {
            if (mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING) {
                mediaPlayer.pause();
            } else {
                mediaPlayer.play();
            }
        });

        HBox controls = new HBox(10, progressBar, fastRewind, pause, fastForward, timeLabel, progressLabel);
        controls.setAlignment(Pos.CENTER);
        HBox.setHgrow(progressBar, Priority.ALWAYS);

        // Set up media view and layout
        MediaView mediaView = new MediaView(mediaPlayer);
        mediaView.setPreserveRatio(true);
        mediaView.fitWidthProperty().bind(primaryStage.widthProperty());
        mediaView.fitHeightProperty().bind(primaryStage.heightProperty().subtract(controls.heightProperty()));

        VBox videoBox = new VBox();
        videoBox.getChildren().addAll(mediaView, controls);

        // Set up and show the scene.
        Scene scene = new Scene(videoBox, 800, 450);
        primaryStage.setTitle("Sean Player");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void updateProgress() {
        Duration bufferEnd = mediaPlayer.getBufferProgressTime();
        Duration mediaDuration = mediaPlayer.getMedia().getDuration();
        double progress = bufferEnd.toMillis() / mediaDuration.toMillis();
        progressProperty.set(progress);

        Duration currentTime = mediaPlayer.getCurrentTime();
        String formattedCurrentTime = String.format("%02d:%02d", (int) currentTime.toMinutes(), (int) currentTime.toSeconds() % 60);
        String formattedTotalTime = String.format("%02d:%02d", (int) mediaDuration.toMinutes(), (int) mediaDuration.toSeconds() % 60);
        timeLabel.setText(formattedCurrentTime + " / " + formattedTotalTime);
    }
}