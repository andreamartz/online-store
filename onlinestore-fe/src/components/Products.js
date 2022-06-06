// this should show a list of products

// don't need a separated Product component, right?

import {useSelector, useDispatch} from "react-redux";
import {Col, Row} from "react-bootstrap";
import {initiateGetAllProducts} from "../modules/product";
import {useEffect} from "react";

function Products({ _useSelector = useSelector,
                    _useDispatch = useDispatch}) {



    const products = _useSelector(state => state.product?.products);

    return  (
        <>
            <Row>
                {products.map(product => {
                    return (
                        <Col key = {product.id} xs={4} xs={12} sm={8} md={6} lg={4} xl={3} className='mb-3'>
                            {product.name}
                        </Col>
                    )
                })}
            </Row>
        </>
    )
}

export default Products;