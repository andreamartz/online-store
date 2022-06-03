import React from 'react';
import ReactDOM from 'react-dom/client';
import './index.css';
import App from './App';
import {applyMiddleware, combineReducers, compose, createStore} from "redux";
import user from './modules/user';
import product from './modules/product';
import {Provider} from "react-redux";
import 'bootstrap/dist/css/bootstrap.min.css';

// const handleAsync = storeAPI => next => action => {
//     if (typeof action === 'function')
//         return action(storeAPI.dispatch, storeAPI.getState)
//
//     next(action)
// }

// see https://redux.js.org/tutorials/fundamentals/part-6-async-logic#writing-an-async-function-middleware
// handleAsync is passed in to compos
function handleAsync(storeAPI) {
    return function(next) {
        return function (action) {
            if (typeof action === 'function')

                return action(storeAPI.dispatch, storeAPI.getState)

            next(action)
        }
    }
}

// definition of store enhancer: https://redux.js.org/understanding/thinking-in-redux/glossary#store-enhancer
// compose functions video (first 5 minutes): https://www.youtube.com/watch?v=kclGXphtmVg&t=62s
const composeEnhancers = window.__REDUX_DEVTOOLS_EXTENSION_COMPOSE__ || compose;

const store = createStore(
    combineReducers({user, product}),
    composeEnhancers(applyMiddleware(handleAsync))
)

const root = ReactDOM.createRoot(document.getElementById('root'));
root.render(
    <React.StrictMode>
        <Provider store={store}>
            <App/>
        </Provider>
    </React.StrictMode>
);