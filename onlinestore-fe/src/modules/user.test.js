import reducer, {initiateLogin, LOGIN_FAILURE, LOGIN_START, LOGIN_SUCCESS, LOGOUT, UPDATE_CREDENTIALS} from "./user";

it('should state w/ a null token', () => {
    const state = reducer()
    expect(state.token).toBeNull()
})

it('should start not pending a login', () => {
    const state = reducer()
    expect(state.loginPending).toBe(false)
})

it('should set loginPending true when LOGIN_START', () => {
    const initialState = reducer()
    const state = reducer(initialState, {type: LOGIN_START})
    expect(state.loginPending).toBe(true)
})

it('should set loginPending false and token when LOGIN_SUCCESS', () => {
    const initialState = reducer()
    initialState.loginPending = true
    const token = 'some token'
    const state = reducer(initialState, {type: LOGIN_SUCCESS, payload: token})
    expect(state.loginPending).toBe(false)
    expect(state.token).toBe(token)
})

it('should set loginPending false when LOGIN_FAILURE', () => {
    const initialState = reducer()
    initialState.loginPending = true
    const state = reducer(initialState, {type: LOGIN_FAILURE})
    expect(state.loginPending).toBe(false)
})

it('should start with blank credentials', () => {
    const state = reducer()
    expect(state.credentials).toStrictEqual({username: '', password: ''})
})

it('should update credentials when UPDATE_CREDENTIALS', () => {
    const initialState = reducer()
    const payload = {username: 'some username', password: 'some password'}
    const state = reducer(initialState, {type: UPDATE_CREDENTIALS, payload})
    expect(state.credentials).toStrictEqual(payload)
})

it('should set token to null and credentials to blank when LOGOUT', () => {
    const initialState = reducer()
    initialState.token = 'some token'
    initialState.credentials = {username: 'some username', password: 'some password'}
    const state = reducer(initialState, {type: LOGOUT})
    expect(state.credentials).toStrictEqual({username: '', password: ''})
    expect(state.token).toBeNull()
})

it('should dispatch LOGIN_START then LOGIN_FAILURE when initiateLogin w/ bad creds', async () => {
    const username = 'some username'
    const password = 'some password'
    const url = `http://localhost:8081/login?username=${username}&password=${password}`
    let _url

    const mockFetch = (url) => {
        _url = url
        return new Promise(resolve => resolve({ok: false}))
    }

    const dispatch = jest.fn()
    const state = {user: {credentials: {username, password}}}
    const getState = () => state
    const sideEffect = initiateLogin(mockFetch)
    await sideEffect(dispatch, getState)
    expect(_url).toBe(url)
    expect(dispatch).toHaveBeenCalledWith({type: LOGIN_START})
    expect(dispatch).toHaveBeenCalledWith({type: LOGIN_FAILURE})
})

it('should dispatch LOGIN_START then LOGIN_SUCCESS when initiateLogin w/ good creds', async () => {
    const username = 'some username'
    const password = 'some password'
    const token = 'some token'
    const url = `http://localhost:8081/login?username=${username}&password=${password}`
    let _url

    const mockFetch = (url) => {
        _url = url
        return new Promise(resolve => resolve({
            ok: true,
            json: () => new Promise(res => res(token))
        }))
    }

    const dispatch = jest.fn()
    const state = {user: {credentials: {username, password}}}
    const getState = () => state
    await initiateLogin(mockFetch)(dispatch, getState)
    expect(_url).toBe(url)
    expect(dispatch).toHaveBeenCalledWith({type: LOGIN_START})
    expect(dispatch).toHaveBeenCalledWith({type: LOGIN_SUCCESS, payload: token})
})

