import styled from "styled-components";
import { Box } from "@material-ui/core";

export const StatBox = styled(Box)`
    display: flex;
    flex-direction: column;
    align-items: center;
    border: 3px solid #4F4846;
    background-color: #E8CEBF;
    padding: 0px 5%;
    height: 50%;
    margin: 0px 2%;
    > * {
        margin: 5px 0px;
    }
`
