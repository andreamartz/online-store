// Actions
export const LOGIN_START = 'onlinestore/user/LOGIN_START';
export const LOGIN_SUCCESS = 'onlinestore/user/LOGIN_SUCCESS';
export const LOGIN_FAILURE = 'onlinestore/user/LOGIN_FAILURE';
export const UPDATE_CREDENTIALS = 'onlinestore/user/UPDATE_CREDENTIALS';
export const LOGOUT = 'onlinestore/user/LOGOUT';
export const GET_ALL_USERS_START = 'onlinestore/user/GET_ALL_USERS_START';
export const GET_ALL_USERS_SUCCESS = 'onlinestore/user/GET_ALL_USERS_SUCCESS';
export const GET_ALL_USERS_FAILURE = 'onlinestore/user/GET_ALL_USERS_FAILURE';

// InitialState
const initialState = {
    currentUser: null,
    users: [],
    token: null,

    // pending
    loginPending: false,
    registerPending: false,

    // form state
    loginCredentials: {username: '', password: ''},
    registerCredentials: {username: '', password: ''},

    // show
    showLogin: false,
    showRegister: true,

    // error messages
    loginError: "",
    registerError: "",

    // other messages
    setupMessage: "Please register the first owner",
}

// Reducer
export default function reducer(state = initialState, action) {
    switch (action?.type) {
        // case REGISTER_START:
        // case REGISTER_SUCCESS:
        // case REGISTER_FAILURE:
        case LOGIN_START:
            return {
                ...state,
                loginPending: true
            }

        case LOGIN_SUCCESS:
            return {
                ...state,
                token: action.payload,
                // currentUser:
                loginPending: false,
                loginError: "",
            }

        case LOGIN_FAILURE:
            return {
                ...state,
                loginPending: false,
                // loginError:
            }

        case UPDATE_CREDENTIALS:
            return {
                ...state,
                loginCredentials: {
                    username: action.payload.username,
                    password: action.payload.password
                }
            }

        case LOGOUT:
            return {
                ...state,
                loginCredentials: {
                    username: '',
                    password: ''
                },
                token: null
            }

        default:
            return {...state}
    }
}

// Side Effects
export function initiateLogin(_fetch=fetch) {
    return async function sideEffect(dispatch, getState) {
        dispatch({type: LOGIN_START})
        const {username, password} = getState().user.loginCredentials
        const url = `http://localhost:8081/login?username=${username}&password=${password}`
        const response = await _fetch(url)

        if (response.ok) {
            const token = await response.json()
            dispatch({type: LOGIN_SUCCESS, payload: token})
        } else
            dispatch({type: LOGIN_FAILURE})
    }
}

export function initiateGetAllUsers(token, _fetch=fetch) {
    return async function sideEffect(dispatch) {
        dispatch({type: GET_ALL_USERS_START});
        const url = `http://localhost:8081/getAllUsers?token=${token}`;
        const response = await _fetch(url)

        if (response.ok) {
            const result = await response.json();
            dispatch({type: GET_ALL_USERS_SUCCESS, payload: result})
        } else
            dispatch({type: GET_ALL_USERS_FAILURE});
    }
}