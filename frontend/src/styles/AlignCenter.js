import styled from "styled-components";

export const AlignCenter = styled.div`
    min-height: 100vh;
    max-height: 100vh;
    overflow-y: auto;
    display: flex;
    align-items: center;
    justify-content: center;
    background: rgb(255, 193, 166);
    ${props => !props.removeBg ? "background: linear-gradient(135deg, rgba(255, 193, 166, 1) 30%, rgba(224, 253, 220, 1) 75%);" : "background-color: #FFF0EB;"}
`;
