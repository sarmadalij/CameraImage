# Camera Image App

Camera Image App is an Android application that allows users to capture images from the camera, store them in temporary app storage, and display both full-sized and thumbnail images.

## Features

- Capture images using the device camera.
- Store captured images in temporary app storage.
- Display full-sized and thumbnail images in separate ImageViews.

## How It Works

### Camera Capture: 

When you click the "Open Camera" button, the app uses the device camera to capture an image.

### Storage: 

The captured image is stored in temporary app storage. The full-sized image is saved with a unique filename.

### Display: 

The full-sized image is displayed in one ImageView, and a thumbnail version of the image is displayed in another ImageView.

### Thumbnail Size: 

The quality of the thumbnail can be adjusted by modifying the inSampleSize option during decoding. This controls the downsampling of the image to create a smaller thumbnail.

### Permissions: 

The app requests the camera permission 

android.hardware.camera

android.permission.CAMERA
