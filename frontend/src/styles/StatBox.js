import styled from "styled-components";
import { Box } from "@material-ui/core";

export const StatBox = styled(Box)`
    display: flex;
    flex-direction: column;
    align-items: center;
    border: 3px solid #FFE5DD;
    border-radius: 20px;
    background-color: white;
    padding: 0% 6%;
    font-size: 1.5em;
    font-weight: bold;
    color: #FF845B;
    margin: 0px 2%;
    @media (max-width: 1200px) {
        margin: 5% 2%;
    }
`;
