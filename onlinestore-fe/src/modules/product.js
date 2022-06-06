// Actions
export const GET_PRODUCTS_START = "onlinestore/product/GET_PRODUCTS_START";
export const GET_PRODUCTS_SUCCESS = "onlinestore/product/GET_PRODUCTS_SUCCESS";
export const GET_PRODUCTS_FAILURE = "onlinestore/product/GET_PRODUCTS_FAILURE";
// export const GET_PRODUCTS_CANCEL = "onlinestore/product/GET_PRODUCTS_CANCEL"
// ADD_START
// ADD_SUCCESS
// ADD_FAILURE
// EDIT_START
// EDIT_SUCCESS
// EDIT_FAILURE
// DELETE_START
// DELETE_SUCCESS
// DELETE_FAILURE



// InitialState
const initialState = {
    // products: [{id: 1, name: "flowers"}, {id: 2, name: "tree"}],
    products: [],
    // pending
    getAllProductsPending: false,
    // addProductPending: false,
    // editProductPending: false,
    // deleteProductPending: false,
    // -- no pendingViewProducts, right?

    // form state
    // productNameToAdd: "",
    // productNameToEdit: "",
    // productNameToDelete: "",

    // show

    // error messages
    getAllProductsError: "",
    // addProductError: "",
    // editProductError: "",
    // deleteProductError: "",

    // other messages
}



// Reducer
export default function reducer(state = initialState, action) {
    switch(action?.type) {
        case GET_PRODUCTS_START:
            return {
                ...state,
                getAllProductsPending: true,
            }

        case GET_PRODUCTS_SUCCESS:
            return {
                ...state,
                products: [...state.products, ...action.payload],
                getAllProductsPending: false
            }

        case GET_PRODUCTS_FAILURE:
            return {
                ...state,
                getAllProductsPending: false
            }

        default:
            return {...state}

    }
}


// Side effects
export function initiateGetAllProducts(token, _fetch=fetch) {
    return async function sideEffect(dispatch) {
        dispatch({type: GET_PRODUCTS_START});
        const url = `http://localhost:8080/getAllProducts?token=${token}`;
        const response = await _fetch(url);

        if (response.ok) {
            const result = await response.json();
            dispatch({type: GET_PRODUCTS_SUCCESS, payload: result})
        } else {
            dispatch({type: GET_PRODUCTS_FAILURE});
        }
    }
}













// Alex did this to get all users:
// export function initiateGetUsers(_fetch=fetch) {
//     return async function sideEffect(dispatch, getState) {
//         dispatch({type: GET_USERS_START})
//         const token = getState().users.token
//         const url = "http//localhost:8081/getAllUsers?token=${token}"
//         const response = await _fetch(url)
//         if (response.ok) {
//             const data = await _fetch(url)
//             if (response.ok) {
//                 const data = await response.json()
//                 dispatch({type: GET_USERS_SUCCESS, payload: data})
//                 dispatch({type: GET_USERS_VIEW})
//             } else {
//                 dispatch({type: GET_USERS_FAILURE})
//             }
//         }
//     }
// }

// export function initiateAdd

// export function initiateEdit

// export function initiateDelete