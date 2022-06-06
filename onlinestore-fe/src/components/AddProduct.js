// access to adding a product must be restricted to owners
import {useDispatch, useSelector} from "react-redux";

function AddProduct({
                        _useSelector=useSelector,
                        _useDispatch=useDispatch
                    }) {
    const dispatch = _useDispatch();
    const users = _useSelector(state => state.users);

    return (

    )

}

export default AddProduct;