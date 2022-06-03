// Actions
export const LOGIN_START = 'fizzbuzz/user/LOGIN_START'
export const LOGIN_SUCCESS = 'fizzbuzz/user/LOGIN_SUCCESS'
export const LOGIN_FAILURE = 'fizzbuzz/user/LOGIN_FAILURE'
export const UPDATE_CREDENTIALS = 'fizzbuzz/user/UPDATE_CREDENTIALS'
export const LOGOUT = 'fizzbuzz/user/LOGOUT'

// InitialState
const initialState = {
    token: null,
    loginPending: false,
    credentials: {username: '', password: ''}
}

// Reducer
export default function reducer(state = initialState, action) {
    switch (action?.type) {
        case LOGIN_START:
            return {
                ...state,
                loginPending: true
            }

        case LOGIN_SUCCESS:
            return {
                ...state,
                token: action.payload,
                loginPending: false
            }

        case LOGIN_FAILURE:
            return {
                ...state,
                loginPending: false
            }

        case UPDATE_CREDENTIALS:
            return {
                ...state,
                credentials: {
                    username: action.payload.username,
                    password: action.payload.password
                }
            }

        case LOGOUT:
            return {
                ...state,
                credentials: {
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
        const {username, password} = getState().user.credentials
        const url = `http://localhost:8081/login?username=${username}&password=${password}`
        const response = await _fetch(url)

        if (response.ok) {
            const token = await response.json()
            dispatch({type: LOGIN_SUCCESS, payload: token})
        } else
            dispatch({type: LOGIN_FAILURE})
    }
}