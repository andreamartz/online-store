// component should allow both owners and customers to login
// on login, add the current user to user state






import {Button, Card, Form} from "react-bootstrap";
import {useDispatch, useSelector} from "react-redux";
import {initiateLogin, UPDATE_CREDENTIALS} from "../modules/user";

function Login({ hidden = false,
                   _useDispatch = useDispatch,
                   _useSelector = useSelector
               }) {
    const loginCredentials = _useSelector(state => state.user.loginCredentials)
    const loginPending = _useSelector(state => state.user.loginPending)
    const dispatch = _useDispatch();

    // const loginErrorMessage = _useSelector(state => state.loginErrorMessage);

    function updateUsername(username) {
        dispatch({type: UPDATE_CREDENTIALS, payload: {...loginCredentials, username}})
    }

    function updatePassword(password) {
        dispatch({type: UPDATE_CREDENTIALS, payload: {...loginCredentials, password}})
    }

    function handleSubmit(event) {
        event.preventDefault();
        dispatch(initiateLogin())
    }

    // return <Form onSubmit={handleSubmit}>
    //     <Form.Control placeholder='Username' onChange={e => updateUsername(e.target.value)}/>
    //     <Form.Control placeholder='Password' onChange={e => updatePassword(e.target.value)}/>
    //     <Button type='submit' disabled={loginPending}>Login</Button>
    // </Form>
    return (
        <Form className={"d-grid"} onSubmit={handleSubmit}>
            <Form.Group className={"mb-3"}>
                <Form.Label>Username</Form.Label>
                <Form.Control type={"text"}
                              placeholder={"Username"}
                              onChange={event => updateUsername(event.target.value)}
                              // value={username}
                              // isInvalid={submitted && !username}
                />
            </Form.Group>

            <Form.Group className={"mb-3"}>
                <Form.Label>Password</Form.Label>
                <Form.Control type={"password"}
                              placeholder={"Password"}
                              onChange={event => updatePassword(event.target.value)}
                              // value={password}
                              // isInvalid={submitted && !password}
                />
            </Form.Group>

            <Button variant="primary" type="submit"
                    disabled={loginPending}
                    data-testid={"logInButton"}>
                Log in
            </Button>
        </Form>
    )
}

export default Login;