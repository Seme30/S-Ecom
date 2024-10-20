# S-Ecom Android App

## Overview

This Android application is built using Kotlin and Jetpack Compose. The app focuses on a clean and intuitive user interface, clean architecture, and efficient ui state handling.

## Features

* **Product Browsing:** Explore a catalog of products with clear images, descriptions, and pricing.
* **Product Details:** View comprehensive information about individual products, including ratings and reviews.
* **Category Filtering:** Browse products by category for easy navigation.
* **Dark Mode Support:** Switch between light and dark themes for personalized viewing.
* **User Authentication:** uses Firebase authentication using email and password.

## Technologies & Libraries

* **Kotlin:** The primary programming language for Android development, known for its conciseness and safety.
* **Jetpack Compose:** A modern declarative UI toolkit for building native Android interfaces.
* **Hilt:** Dependency injection library for managing dependencies and improving code testability.
* **Retrofit:** Type-safe HTTP client for making network requests to the API.
* **Coil:** Image loading library for efficiently displaying images from URLs.
* **Coroutines:** Kotlin's concurrency framework for handling asynchronous operations like network calls.
* **DataStore:** Jetpack library for storing key-value data preferences, like theme settings.
* **Navigation Component:** Jetpack library for managing navigation between screens and handling deep links.

## How to Run the Code

1. **Clone the Repository:**

2. **Open in Android Studio:** Import the project into Android Studio.

3. **Build and Run:** Build the project and run it on an Android emulator or a physical device.

## Project Structure

* **`ui`:** Contains composable functions for building the user interface.
    * **`screens`:** Individual screen composables (e.g., `ProductListScreen`, `ProductDetailScreen`, `ProfileScreen`).
    * **`components`:** Reusable UI components (e.g., `ProductCard`, `CategoryItem`).

* **`data`:** Handles data fetching, storage, and modeling.
    * **`repository`:** Repositories for interacting with data sources (e.g., `ProductRepository`).
    * **`model`:** Data classes representing the app's data models (e.g., `Product`, `Category`).

* **`di`:** Dependency injection modules for providing dependencies using Hilt.

* **`util`:** Utility classes and functions for common tasks.


## Screenshots 


![Screenshot 4](https://github.com/Seme30/HuluShop/assets/83661382/829c558e-af8e-4a76-918c-ac9e9743e2db)
![Screenshot 3](https://github.com/Seme30/HuluShop/assets/83661382/22ca4eb3-2449-43f5-8bc9-35f8831ff2fb)
![Screenshot 2](https://github.com/Seme30/HuluShop/assets/83661382/159cfa0d-a85b-4ade-a30e-ca06774bb922)
![Screenshot 1](https://github.com/Seme30/HuluShop/assets/83661382/14a47aec-6616-49a1-a7fb-78cb18d3f8db)
![screenshot 5](https://github.com/Seme30/HuluShop/assets/83661382/12d81962-87ec-4f2a-b328-28fc34ba611f)

