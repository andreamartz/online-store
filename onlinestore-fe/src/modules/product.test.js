import reducer, {
    GET_PRODUCTS_START,
    GET_PRODUCTS_SUCCESS,
    GET_PRODUCTS_FAILURE,
    initiateGetAllProducts,
} from "./product";

/********* INITIAL STATE *********/
it("should initialize getAllProductsPending with false", () => {
    const state = reducer();
    expect(state.getAllProductsPending).toBe(false);
});

// it("should initialize with", () => {
//
// });
//
// it("should initialize with", () => {
//
// });
//
// it("should initialize with", () => {
//
// });
//
// it("should initialize with", () => {
//
// });
//
// it("should initialize with", () => {
//
// });

/********* GET ALL PRODUCTS *********/
it('should set getAllProductsPending true when GET_PRODUCTS_START', () => {
    const initialState = reducer(undefined, {type: GET_PRODUCTS_START});
    expect(initialState.getAllProductsPending).toBe(true);
});

it('should set products and getAllProductsPending false when GET_PRODUCTS_SUCCESS', () => {
    const initialState = reducer();
    initialState.getAllProductsPending = true;
    const products = [{ id: 1, name: "product1" }];

    const state = reducer(initialState, {type: GET_PRODUCTS_SUCCESS, payload: products});
    expect(state.getAllProductsPending).toBe(false);
    expect(state.products).toStrictEqual(products);
});

it('should set getAllProductsPending false when GET_PRODUCTS_FAILURE', () => {

});

/********* initiateGetAllProducts *********/
it("should dispatch GET_PRODUCTS_START w/ token then GET_PRODUCTS_SUCCESS when initiateGetAllProducts and response is ok", async () => {
    const dispatch = jest.fn();
    const token = "some token";
    const products = [{ id: 1, name: "prod1"}, { id: 2, name: "prod2"}];
    const url = `http://localhost:8080/getAllProducts?token=${token}`;
    let _url;  // I NEED to know WHY or leave this out

    // Marissa argues that we are testing that the correct url is being called.
    //     She pointed out that we can't "inject" it into the initiateG... fcn.
    //     _url is the best we can do
    const mockFetch = (url) => {
        // _url = url;
        return new Promise(resolve => resolve({
            ok: true,
            json: () => new Promise(res => res(products))
        }))
    }

    await initiateGetAllProducts(token, mockFetch)(dispatch);
    expect(_url).toBe(url);  // expecting that mockFetch was run?? if so, why not use toHaveBeenCalledWith
    expect(dispatch).toHaveBeenCalledWith({type: GET_PRODUCTS_START});
    expect(dispatch).toHaveBeenCalledWith(({type: GET_PRODUCTS_SUCCESS, payload: products}));
});

it("should dispatch GET_PRODUCTS_START w/ token then GET_PRODUCTS_FAILURE when initiateGetAllProducts and response not ok", async () => {
    const dispatch = jest.fn();
    const token = "some token";
//     const products = [{ id: 1, name: "prod1"}, { id: 2, name: "prod2"}];
    const url = `http://localhost:8080/getAllProducts?token=${token}`;
    let _url;

    const mockFetchNotOK = (url) => {
        _url = url;
        return new Promise(((resolve, reject) => {
            resolve({
                ok: false,
            })
        }))
    }

    await initiateGetAllProducts(token, mockFetchNotOK)(dispatch);
    expect(_url).toBe(url);
    expect(dispatch).toHaveBeenCalledWith({type: GET_PRODUCTS_START});
    expect(dispatch).toHaveBeenCalledWith(({type: GET_PRODUCTS_FAILURE}));
});

/********* from jsproj1 *********/
// const _fetch_not_ok = (url) => {
//     return new Promise(((resolve, reject) => {
//         resolve({
//             ok: false
//         })
//     }))
// }
//
// async function getUsersIPAddress(_fetch = fetch) {
//     const iPResult = await _fetch(iPAddressUrl);
//     if (iPResult.ok) {
//         return await iPResult.json();
//     }
//     return false;
// }

/********** IP ADDRESS *************/

/********* E *********/

/********* E *********/

