<?php

use App\Http\Controllers\ProductController;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Route;

/*
|--------------------------------------------------------------------------
| API Routes
|--------------------------------------------------------------------------
|
| Here is where you can register API routes for your application. These
| routes are loaded by the RouteServiceProvider within a group which
| is assigned the "api" middleware group. Enjoy building your API!
|
*/

Route::middleware('auth:sanctum')->get('/user', function (Request $request) {
    return $request->user();
});

Route::get('products', [ProductController::class,'getProductsAPI']);
Route::post('products', [ProductController::class,'addOrEditProductAPI']);
Route::get('products/{id}', [ProductController::class,'getProductByIdAPI']);
Route::put('products/{id}', [ProductController::class,'addOrEditProductAPI']);
Route::delete('products/{id}', [ProductController::class,'deleteProductAPI']);
Route::post('upload-image', [ProductController::class,'uploadFileAPI']);
