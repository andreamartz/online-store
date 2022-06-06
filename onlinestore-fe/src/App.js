import './App.css';
import {useDispatch, useSelector} from "react-redux";
import Login from "./components/Login";
import Products from "./components/Products";
import {useEffect} from "react";
import {initiateGetAllProducts} from "./modules/product";
import {initiateGetAllUsers, initiateLogin} from "./modules/user";
import {Spinner} from "react-bootstrap";

function App({_useSelector=useSelector,
              _useDispatch=useDispatch,
              _initiateGetAllProducts=initiateGetAllProducts,
              _initiateGetAllUsers=initiateGetAllUsers,
              LoginC=Login}) {

    const dispatch = _useDispatch();
    dispatch(initiateGetAllProducts());

    const token = _useSelector(state => state.user.token);
    const getAllUsersPending = _useSelector(state => state.user.getAllUsersPending);
    const getAllProductsPending = _useSelector(state => state.product.getAllProductsPending);



    if (getAllProductsPending || getAllUsersPending) {
        return (
            <Spinner animation="border" role="status">
                <span className="visually-hidden">Loading...</span>
            </Spinner>
        )
    }
    return (
        <>
            <LoginC/>
            <Products />
        </>
    );
    // if no currentUser and no users in db, show owner login page
}

export default App;
