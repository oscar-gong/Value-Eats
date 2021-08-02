import styled from "styled-components";

export const AlignCenter = styled.div`
    display: flex;
    align-items: center;
    justify-content: center;
    background: rgb(255, 193, 166);
    min-height: 100vh;
    ${props => !props.removeBg ? "background: linear-gradient(135deg, rgba(255, 193, 166, 1) 30%, rgba(224, 253, 220, 1) 75%);" : "background-color: #F7FCF5;"}
`;
