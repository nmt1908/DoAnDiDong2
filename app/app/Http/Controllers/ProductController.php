<?php

namespace App\Http\Controllers;

use App\Models\Product;
use Illuminate\Http\Request;
use Illuminate\Support\Str;


use function PHPUnit\Framework\isEmpty;

class ProductController extends Controller
{
    private $FILE_PATH_SRC = "D:/wamp64/www/photo";
    private $ARRAY_FILE_EXTENSION = ['jpeg','png','jpg'];


    /*
    * Main methods
    */

    public function addOrEditProductAPI(Request $request)
    {
        $result = '';
        /**
         * $data get request parameters
         * if validation successful => $data get $request->all()
         * else return error message
         */
        $result = $this->checkValidateDataProduct($request);

        if ($result != null && isset($result)) {
            if (isset($request->id)) {
                $product = Product::find($request->id);
                $this->setDataToProduct($result, $product);
                return response($product, 201);
            } else {
                $product = new Product;
                $this->setDataToProduct($result, $product);
                return response($product, 201);
            }
        }
        return response($result, 400);
    }

    public function getProductsAPI()
    {
        $result = '';
        $result = Product::all();
        if ($result != null) {
            return response($result, 200);
        }
        return response($result, 400);
    }

    public function getProductByIdAPI(Request $request)
    {
        $result = '';
        $result = Product::find($request->id);
        if ($result != null) {
            return response($result, 200);
        }
        return response($result, 400);
    }

    public function deleteProductAPI(Request $request)
    {
    }

    public function uploadFileAPI(Request $request)
    {
        $result = '';
        if($this->checkValidateDataImage($request->file('image'))){
            $imageName = time().'.'.$request->image->extension();
            $result = $imageName;
            $request->image->move($this->FILE_PATH_SRC, $imageName);
            return $result;

        }else{
            return response($result , 400);
        }
    }


    /*
    * Widget methods
    */

    private function checkValidateDataProduct($request)
    {
        if (!isset($request->name) || !isset($request->image) || !isset($request->quantity) || !isset($request->price) || !isset($request->description)) {
            return null;
        }

        if ($request->quantity < 1 || $request->price < 1) {
            return null;
        }
        return $request->all();
    }

    private function setDataToProduct($request, $product)
    {
        $product->name = $request['name'];
        $product->description = $request['description'];
        $product->price = $request['price'];
        $product->quantity = $request['quantity'];
        $product->image = $request['image'];
        $product->save();
    }

    private function checkValidateDataImage($imageFile)
    {
        if(!isset($imageFile)){
            return null;
        }
        
        $originOfFile = explode("/",$imageFile->getClientMimeType());
        if($originOfFile[0] != "image"){
            return null;
        }else{
            if(!in_array($originOfFile[1],$this->ARRAY_FILE_EXTENSION,false)){
                return null;
            }
        }
        return true;
    }
}
